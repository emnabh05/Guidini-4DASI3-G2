import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Commentaire, Post, StatutPost } from '../post.model';
import { PostService, messageErreur } from '../post.service';

interface FilDiscussion { c: Commentaire; niveau: number; }

const CLE_AUTEUR = 'ms6.idAuteur';

@Component({
  selector: 'app-communaute',
  imports: [FormsModule, DatePipe],
  templateUrl: './communaute.html',
  styleUrl: './communaute.scss',
})
export class Communaute implements OnInit {
  private readonly api = inject(PostService);

  readonly filtres: { label: string; statut: StatutPost | null }[] = [
    { label: 'Tous', statut: null },
    { label: 'Publiés', statut: 'PUBLIE' },
    { label: 'Brouillons', statut: 'BROUILLON' },
    { label: 'Archivés', statut: 'ARCHIVE' },
  ];
  readonly libelleStatut: Record<StatutPost, string> = { PUBLIE: 'Publié', BROUILLON: 'Brouillon', ARCHIVE: 'Archivé' };

  // ---------- état ----------
  posts = signal<Post[]>([]);
  filtre = signal<StatutPost | null>(null);
  chargement = signal(true);
  erreur = signal('');
  info = signal('');

  /** Identifiant de l'utilisateur courant (en attendant l'authentification ms1). */
  idAuteur = signal(this.lireAuteur());

  // création
  nouveau = { titre: '', contenu: '', imageUrl: '', brouillon: false };
  envoiPost = signal(false);
  composerOuvert = signal(false);

  // édition d'un post
  enEdition = signal<number | null>(null);
  edition = { titre: '', contenu: '', imageUrl: '' };

  // commentaires, par post
  ouverts = signal<Set<number>>(new Set());
  commentaires = signal<Record<number, Commentaire[]>>({});
  saisie: Record<number, string> = {};
  reponseA = signal<Record<number, Commentaire | null>>({});
  commentaireEdite = signal<number | null>(null);
  texteEdite = '';

  nbPosts = computed(() => this.posts().length);

  ngOnInit() { this.charger(); }

  // ---------- posts ----------
  charger() {
    this.chargement.set(true);
    this.erreur.set('');
    this.api.getPosts(this.filtre()).subscribe({
      next: (p) => { this.posts.set(p); this.chargement.set(false); },
      error: (e) => { this.erreur.set(messageErreur(e)); this.chargement.set(false); },
    });
  }

  choisirFiltre(statut: StatutPost | null) {
    this.filtre.set(statut);
    this.charger();
  }

  changerAuteur(valeur: number) {
    const id = Math.max(1, Math.floor(Number(valeur) || 1));
    this.idAuteur.set(id);
    try { localStorage.setItem(CLE_AUTEUR, String(id)); } catch { /* stockage indisponible */ }
  }

  publier(form: NgForm) {
    if (form.invalid) { form.control.markAllAsTouched(); return; }
    this.envoiPost.set(true);
    this.api.creerPost({
      idAuteur: this.idAuteur(),
      titre: this.nouveau.titre.trim(),
      contenu: this.nouveau.contenu.trim(),
      imageUrl: this.nouveau.imageUrl.trim() || null,
      statut: this.nouveau.brouillon ? 'BROUILLON' : 'PUBLIE',
    }).subscribe({
      next: () => {
        this.envoiPost.set(false);
        this.notifier(this.nouveau.brouillon ? 'Brouillon enregistré ✓' : 'Publication en ligne ✓');
        form.resetForm({ titre: '', contenu: '', imageUrl: '', brouillon: false });
        this.composerOuvert.set(false);
        this.charger();
      },
      error: (e) => { this.envoiPost.set(false); this.erreur.set(messageErreur(e)); },
    });
  }

  ouvrirEdition(p: Post) {
    this.enEdition.set(p.idPost);
    this.edition = { titre: p.titre, contenu: p.contenu, imageUrl: p.imageUrl ?? '' };
  }

  enregistrerEdition(p: Post) {
    if (!this.edition.titre.trim() || !this.edition.contenu.trim()) return;
    this.api.modifierPost(p.idPost, {
      titre: this.edition.titre.trim(),
      contenu: this.edition.contenu.trim(),
      imageUrl: this.edition.imageUrl.trim() || null,
    }).subscribe({
      next: (maj) => { this.remplacer(maj); this.enEdition.set(null); this.notifier('Publication modifiée ✓'); },
      error: (e) => this.erreur.set(messageErreur(e)),
    });
  }

  changerStatut(p: Post, statut: StatutPost) {
    this.api.changerStatut(p.idPost, statut).subscribe({
      next: (maj) => {
        // si un filtre est actif et que le post n'y correspond plus, on le retire de la liste
        if (this.filtre() && maj.statut !== this.filtre()) {
          this.posts.update((l) => l.filter((x) => x.idPost !== maj.idPost));
        } else {
          this.remplacer(maj);
        }
        this.notifier(`Statut : ${this.libelleStatut[statut]}`);
      },
      error: (e) => this.erreur.set(messageErreur(e)),
    });
  }

  supprimer(p: Post) {
    if (!confirm(`Supprimer « ${p.titre} » et tous ses commentaires ?`)) return;
    this.api.supprimerPost(p.idPost).subscribe({
      next: () => { this.posts.update((l) => l.filter((x) => x.idPost !== p.idPost)); this.notifier('Publication supprimée'); },
      error: (e) => this.erreur.set(messageErreur(e)),
    });
  }

  // ---------- commentaires ----------
  basculerCommentaires(p: Post) {
    const set = new Set(this.ouverts());
    if (set.has(p.idPost)) { set.delete(p.idPost); this.ouverts.set(set); return; }
    set.add(p.idPost);
    this.ouverts.set(set);
    this.chargerCommentaires(p.idPost);
  }

  chargerCommentaires(idPost: number) {
    this.api.getCommentaires(idPost).subscribe({
      next: (c) => this.commentaires.update((m) => ({ ...m, [idPost]: c })),
      error: (e) => this.erreur.set(messageErreur(e)),
    });
  }

  /** Commentaires dans l'ordre de lecture : chaque réponse sous son parent, avec son niveau d'indentation. */
  fil(idPost: number): FilDiscussion[] {
    const tous = this.commentaires()[idPost] ?? [];
    const enfants = new Map<number | null, Commentaire[]>();
    for (const c of tous) {
      const cle = c.idCommentaireParent ?? null;
      enfants.set(cle, [...(enfants.get(cle) ?? []), c]);
    }
    const res: FilDiscussion[] = [];
    const parcourir = (parent: number | null, niveau: number) => {
      for (const c of enfants.get(parent) ?? []) {
        res.push({ c, niveau: Math.min(niveau, 4) });
        parcourir(c.idCommentaire, niveau + 1);
      }
    };
    parcourir(null, 0);
    return res;
  }

  repondre(p: Post, c: Commentaire | null) {
    this.reponseA.update((m) => ({ ...m, [p.idPost]: c }));
  }

  commenter(p: Post) {
    const texte = (this.saisie[p.idPost] ?? '').trim();
    if (!texte) return;
    const parent = this.reponseA()[p.idPost];
    this.api.commenter(p.idPost, {
      idAuteur: this.idAuteur(),
      contenu: texte,
      idCommentaireParent: parent?.idCommentaire ?? null,
    }).subscribe({
      next: () => {
        this.saisie[p.idPost] = '';
        this.repondre(p, null);
        this.chargerCommentaires(p.idPost);
      },
      error: (e) => this.erreur.set(messageErreur(e)),
    });
  }

  editerCommentaire(c: Commentaire) {
    this.commentaireEdite.set(c.idCommentaire);
    this.texteEdite = c.contenu;
  }

  enregistrerCommentaire(c: Commentaire) {
    const texte = this.texteEdite.trim();
    if (!texte) return;
    this.api.modifierCommentaire(c.idCommentaire, texte).subscribe({
      next: () => { this.commentaireEdite.set(null); this.chargerCommentaires(c.idPost); },
      error: (e) => this.erreur.set(messageErreur(e)),
    });
  }

  supprimerCommentaire(c: Commentaire) {
    if (!confirm('Supprimer ce commentaire et ses réponses ?')) return;
    this.api.supprimerCommentaire(c.idCommentaire).subscribe({
      next: () => this.chargerCommentaires(c.idPost),
      error: (e) => this.erreur.set(messageErreur(e)),
    });
  }

  // ---------- utilitaires ----------
  initiale(id: number) { return '#' + id; }

  private remplacer(maj: Post) {
    this.posts.update((l) => l.map((x) => (x.idPost === maj.idPost ? maj : x)));
  }

  private notifier(msg: string) {
    this.erreur.set('');
    this.info.set(msg);
    setTimeout(() => this.info.set(''), 2500);
  }

  private lireAuteur(): number {
    try {
      const user = JSON.parse(localStorage.getItem('user') ?? 'null');
      if (user?.id) return Number(user.id);
      return Number(localStorage.getItem(CLE_AUTEUR)) || 1;
    } catch { return 1; }
  }
}
