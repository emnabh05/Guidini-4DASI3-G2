import { NgTemplateOutlet } from '@angular/common';
import { afterNextRender, Component, computed, ElementRef, inject, signal, viewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NavbarPrestataire } from './navbar-prestataire';

type Statut = 'Publiée' | 'En cours' | 'En attente' | 'Terminée' | 'Annulée';
type Type = 'Guide' | 'Chauffeur';
type ModeDialogue = 'creation' | 'details' | 'recherche';

interface MissionAgence {
  id: number;
  titre: string;
  description: string;
  lieu: string;
  debut: string;
  fin: string;
  langue: string;
  type: Type;
  postes: number;
  candidatures: number;
  statut: Statut;
  image: string;
}

const dates = new Intl.DateTimeFormat('fr-FR', { day: 'numeric', month: 'short', year: 'numeric', timeZone: 'UTC' });
const missionsInitiales: MissionAgence[] = [
  { id: 1, titre: 'Visite de Carthage', description: 'Accompagnement touristique', lieu: 'Tunis, Carthage', debut: '2026-12-20', fin: '2026-12-22', langue: 'Français', type: 'Guide', postes: 1, candidatures: 5, statut: 'En cours', image: '/images/carthage.jpg' },
  { id: 2, titre: 'Circuit Sidi Bou Saïd', description: 'Visite et découverte', lieu: 'Sidi Bou Saïd', debut: '2026-12-15', fin: '2026-12-18', langue: 'Français', type: 'Guide', postes: 1, candidatures: 3, statut: 'Publiée', image: '/admin/val3.jpg' },
  { id: 3, titre: 'Excursion Sahara – 3 jours', description: 'Transport et accompagnement', lieu: 'Tozeur, Douz', debut: '2027-01-02', fin: '2027-01-05', langue: 'Français', type: 'Chauffeur', postes: 2, candidatures: 4, statut: 'En attente', image: '/images/sahara.jpg' },
  ...([
    ['Patrimoine de Tunis', 'Tunis', 'Publiée'],
    ['Balade à La Marsa', 'La Marsa', 'En cours'],
    ['Découverte de Carthage', 'Carthage', 'Terminée'],
    ['Circuit culturel à Tunis', 'Tunis', 'Terminée'],
    ['Visite de Sidi Bou Saïd', 'Sidi Bou Saïd', 'Annulée'],
  ] as const).map(([titre, lieu, statut], index): MissionAgence => ({
    id: index + 4, titre, lieu, statut, description: 'Accompagnement touristique', debut: '2026-12-20',
    fin: '2026-12-20', type: 'Guide', langue: 'Français', postes: 1, candidatures: 0, image: '/images/carthage.jpg',
  })),
];

const profils = [
  { id: 1, nom: 'Amine Ben Ali', type: 'Guide', zone: 'Tunis, Carthage', langue: 'Français', tarif: 120 },
  { id: 2, nom: 'Leïla Mansouri', type: 'Guide', zone: 'Sidi Bou Saïd', langue: 'Français', tarif: 140 },
  { id: 3, nom: 'Youssef Trabelsi', type: 'Chauffeur', zone: 'Tozeur, Douz', langue: 'Français', tarif: 350 },
] as const;

@Component({
  selector: 'app-gestion-missions',
  imports: [NavbarPrestataire, FormsModule, NgTemplateOutlet],
  templateUrl: './gestion-missions.html',
  styleUrl: './gestion-missions.scss',
})
export class GestionMissions {
  private readonly dialogue = viewChild.required<ElementRef<HTMLDialogElement>>('dialogue');
  readonly missions = signal(missionsInitiales);
  readonly filtre = signal<Statut | ''>('');
  readonly page = signal(0);
  readonly invitations = signal<{ missionId: number; profilId: number }[]>([]);
  readonly selectee = signal<MissionAgence | null>(null);
  readonly message = signal('');
  readonly taillePage = 3;
  readonly pagePrecedente = (page: number) => page - 1;
  readonly pageSuivante = (page: number) => page + 1;
  readonly filtres: { nom: string; valeur: Statut | '' }[] = [
    { nom: 'Toutes', valeur: '' }, { nom: 'Publiées', valeur: 'Publiée' },
    { nom: 'En cours', valeur: 'En cours' }, { nom: 'Terminées', valeur: 'Terminée' },
    { nom: 'Annulées', valeur: 'Annulée' },
  ];
  readonly legende: { nom: string; valeur: Statut; couleur: string }[] = [
    { nom: 'En cours', valeur: 'En cours', couleur: '#2eb856' },
    { nom: 'Publiées', valeur: 'Publiée', couleur: '#5aa7f7' },
    { nom: 'En attente', valeur: 'En attente', couleur: '#ffc526' },
    { nom: 'Terminées', valeur: 'Terminée', couleur: '#aeb7c9' },
    { nom: 'Annulées', valeur: 'Annulée', couleur: '#f08088' },
  ];
  readonly resultats = computed(() => this.missions().filter(({ statut }) => !this.filtre() || statut === this.filtre()));
  readonly visibles = computed(() => this.resultats().slice(this.page() * this.taillePage, (this.page() + 1) * this.taillePage));
  readonly reponses = computed(() => this.missions().reduce((total, mission) => total + mission.candidatures, 0));
  readonly graphique = computed(() => {
    const total = this.missions().length;
    if (!total) return '#e5ecf8';
    let debut = 0;
    return 'conic-gradient(' + this.legende.map(({ valeur, couleur }) => {
      const fin = debut + (this.compter(valeur) / total) * 100;
      const segment = couleur + ' ' + debut + '% ' + fin + '%';
      debut = fin;
      return segment;
    }).join(', ') + ')';
  });
  modeRecherche: 'manuelle' | 'automatique' = 'manuelle';
  missionRecherche: number | null = null;
  resultatsProfils: (typeof profils)[number][] = [];
  readonly modeDialogue = signal<ModeDialogue>('details');
  formulaire = this.nouvelleMission();
  erreur = '';

  readonly icones: Record<string, string> = {
    brief: 'M3 8h18v12H3zM8 8V5h8v3M3 13h18M11 13v2h2v-2',
    people: 'M15 20v-2a4 4 0 0 0-4-4H7a4 4 0 0 0-4 4v2m18 0v-2a4 4 0 0 0-3-4M12 7a3 3 0 1 1-6 0 3 3 0 0 1 6 0zm5-3a3 3 0 0 1 0 6',
    mail: 'M3 5h18v14H3zM3 6l9 7 9-7',
    check: 'm7 12 3 3 7-7M22 12a10 10 0 1 1-20 0 10 10 0 0 1 20 0z',
    calendar: 'M3 5h18v16H3zM3 10h18M8 3v4M16 3v4M8 14h2m4 0h2m-8 3h2',
    plus: 'M12 4v16M4 12h16',
    pin: 'M20 10c0 5-8 11-8 11S4 15 4 10a8 8 0 1 1 16 0zm-5 0a3 3 0 1 1-6 0 3 3 0 0 1 6 0z',
    person: 'M16 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0zM4 22v-2a8 8 0 0 1 16 0v2',
    car: 'M4 11l2-5h12l2 5v7H4zM4 14h16M5 18v2m14-2v2',
    eye: 'M2 12c2-4 6-7 10-7s8 3 10 7c-2 4-6 7-10 7s-8-3-10-7zm13 0a3 3 0 1 1-6 0 3 3 0 0 1 6 0z',
    search: 'm15 15 5 5M17 10a7 7 0 1 1-14 0 7 7 0 0 1 14 0z',
    bars: 'M3 15h4v6H3zM10 10h4v11h-4zM17 3h4v18h-4z',
  };

  constructor() {
    const route = inject(ActivatedRoute);
    if (route.snapshot.queryParamMap.get('action') === 'creer') {
      afterNextRender(() => this.ouvrir(this.dialogue().nativeElement, 'creation'));
    }
  }

  compter(statut: Statut | ''): number {
    return statut ? this.missions().filter((mission) => mission.statut === statut).length : this.missions().length;
  }

  filtrer(statut: Statut | ''): void {
    this.filtre.set(statut);
    this.page.set(0);
  }

  periode(mission: MissionAgence): string {
    return dates.formatRange(new Date(mission.debut), new Date(mission.fin));
  }

  ouvrir(dialogue: HTMLDialogElement, mode: ModeDialogue, mission?: MissionAgence): void {
    this.erreur = '';
    this.modeDialogue.set(mode);
    this.selectee.set(mission ?? null);
    if (mode === 'creation') this.formulaire = this.nouvelleMission();
    if (mode === 'recherche') {
      this.missionRecherche = this.missions().find(({ statut }) => statut === 'Publiée')?.id ?? null;
      this.trouverProfils();
    }
    dialogue.showModal();
  }

  creer(dialogue: HTMLDialogElement): void {
    const { titre, description, lieu, debut, fin, langue, postes, type } = this.formulaire;
    if (![titre, description, lieu, langue].every((valeur) => valeur.trim()) || !debut || !fin || fin < debut
        || !Number.isInteger(postes) || postes < 1) {
      this.erreur = 'Renseignez les champs et vérifiez les dates et le nombre de postes.';
      return;
    }
    this.missions.update((missions) => [{
      ...this.formulaire, titre: titre.trim(), description: description.trim(), lieu: lieu.trim(), langue: langue.trim(),
      id: Math.max(0, ...missions.map(({ id }) => id)) + 1,
      image: type === 'Chauffeur' ? '/images/sahara.jpg' : '/images/carthage.jpg',
    }, ...missions]);
    this.filtrer('');
    this.message.set('Mission créée dans la démonstration.');
    dialogue.close();
  }

  changerStatut(mission: MissionAgence, statut: 'Publiée' | 'Annulée' | 'Terminée'): void {
    const modifiee = { ...mission, statut };
    this.missions.update((missions) => missions.map((item) => item.id === mission.id ? modifiee : item));
    this.selectee.set(modifiee);
    this.page.update((page) => Math.min(page, Math.max(0, Math.ceil(this.resultats().length / this.taillePage) - 1)));
    this.message.set('Statut de la mission mis à jour.');
  }

  trouverProfils(): void {
    const mission = this.missions().find(({ id }) => id === this.missionRecherche);
    const normaliser = (texte: string) => texte.toLocaleLowerCase('fr').trim();
    this.resultatsProfils = mission ? profils.filter((profil) =>
      this.modeRecherche === 'manuelle' || (
        profil.type === mission.type && normaliser(profil.langue) === normaliser(mission.langue)
        && profil.zone.split(',').some((zone) => normaliser(mission.lieu).includes(normaliser(zone)))
      ),
    ) : [];
  }

  invite(profilId: number): boolean {
    return this.invitations().some((invitation) => invitation.missionId === this.missionRecherche && invitation.profilId === profilId);
  }

  inviter(profilId: number): void {
    const missionId = this.missionRecherche;
    if (missionId === null || this.invite(profilId)) return;
    this.invitations.update((liste) => [...liste, { missionId, profilId }]);
    this.message.set('Invitation ajoutée dans la démonstration.');
  }

  private nouvelleMission(): Omit<MissionAgence, 'id' | 'image'> {
    return { titre: '', description: '', lieu: '', debut: '', fin: '', langue: 'Français', type: 'Guide',
      postes: 1, candidatures: 0, statut: 'En attente' };
  }
}
