import { NgTemplateOutlet } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { OffreProfessionnelle, OffresEtat, TypeOffre } from './offres-etat';

const nomsJours = ['Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam', 'Dim'];
const dateCourte = new Intl.DateTimeFormat('fr-TN', { day: 'numeric', month: 'short', year: 'numeric', timeZone: 'UTC' });
const nomMois = new Intl.DateTimeFormat('fr-FR', { month: 'long', year: 'numeric', timeZone: 'UTC' });
const nombre = new Intl.NumberFormat('fr-TN');

function dateIso(annee: number, mois: number, jour: number): string {
  return new Date(Date.UTC(annee, mois, jour)).toISOString().slice(0, 10);
}

function normaliser(texte: string): string {
  return texte.normalize('NFD').replace(/\p{Diacritic}/gu, '').toLowerCase().trim();
}

@Component({
  selector: 'app-offres-professionnelles',
  imports: [FormsModule, NgTemplateOutlet],
  templateUrl: './offres-professionnelles.html',
  styleUrl: './offres-professionnelles.scss',
})
export class OffresProfessionnelles {
  readonly etat = inject(OffresEtat);
  readonly joursSemaine = nomsJours;
  readonly recherche = signal('');
  readonly type = signal<TypeOffre | ''>('');
  readonly zone = signal('');
  readonly disponibilite = signal('');
  readonly mois = signal({ annee: 2026, numero: 11 });
  readonly editionCalendrier = signal(false);
  readonly joursBrouillon = signal<string[]>([]);
  readonly message = signal('');
  readonly selection = signal<OffreProfessionnelle | null>(null);
  mode: 'voir' | 'modifier' | 'publier' | 'supprimer' = 'voir';
  formulaire = this.nouvelleOffre();
  erreur = '';

  readonly types: { label: string; valeur: TypeOffre | ''; icone: string }[] = [
    { label: 'Toutes', valeur: '', icone: 'person' },
    { label: 'Guides', valeur: 'Guide touristique', icone: 'person' },
    { label: 'Chauffeurs', valeur: 'Chauffeur', icone: 'car' },
  ];
  readonly zones = computed(() => [...new Set(this.etat.offres().map(({ zone }) => zone))].sort());
  readonly moisLibelle = computed(() => {
    const { annee, numero } = this.mois();
    return nomMois.format(new Date(Date.UTC(annee, numero, 1)));
  });
  readonly cases = computed(() => {
    const { annee, numero } = this.mois();
    const depart = (new Date(Date.UTC(annee, numero, 1)).getUTCDay() + 6) % 7;
    const total = new Date(Date.UTC(annee, numero + 1, 0)).getUTCDate();
    return Array.from({ length: depart + total }, (_, index) => {
      const jour = index - depart + 1;
      return jour < 1 ? null : { jour, date: dateIso(annee, numero, jour) };
    });
  });
  readonly joursDisponibles = computed(() => new Set(this.editionCalendrier()
    ? this.joursBrouillon() : this.etat.disponibilites()));
  readonly resultats = computed(() => {
    const recherche = normaliser(this.recherche());
    const { annee, numero } = this.mois();
    const moisDebut = dateIso(annee, numero, 1);
    const moisFin = dateIso(annee, numero + 1, 0);
    return this.etat.offres().filter((offre) => {
      if (this.type() && offre.type !== this.type()) return false;
      if (this.zone() && offre.zone !== this.zone()) return false;
      if (!normaliser([offre.titre, offre.type, offre.zone, offre.description].join(' ')).includes(recherche)) return false;
      if (this.disponibilite() === 'annee') return !offre.debut;
      return this.disponibilite() !== 'mois' || !offre.debut || (offre.debut <= moisFin && offre.fin >= moisDebut);
    });
  });

  readonly icones: Record<string, string> = {
    search: 'm15 15 5 5M17 10a7 7 0 1 1-14 0 7 7 0 0 1 14 0z',
    brief: 'M3 8h18v12H3zM8 8V5h8v3M3 13h18M11 13v2h2v-2',
    pin: 'M20 10c0 5-8 11-8 11S4 15 4 10a8 8 0 1 1 16 0zm-5 0a3 3 0 1 1-6 0 3 3 0 0 1 6 0z',
    calendar: 'M3 5h18v16H3zM3 10h18M8 3v4M16 3v4M8 14h2m4 0h2m-8 3h2',
    plus: 'M12 7v10m-5-5h10M22 12a10 10 0 1 1-20 0 10 10 0 0 1 20 0z',
    person: 'M16 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0zM4 22v-2a8 8 0 0 1 16 0v2',
    car: 'M4 11l2-5h12l2 5v7H4zM4 14h16M5 18v2m14-2v2',
    check: 'm5 12 4 4L19 6',
    wait: 'M6 3h12M6 21h12M7 3v4c0 3 3 4 5 5-2 1-5 2-5 5v4m10-18v4c0 3-3 4-5 5 2 1 5 2 5 5v4',
    eye: 'M2 12c2-4 6-7 10-7s8 3 10 7c-2 4-6 7-10 7s-8-3-10-7zm13 0a3 3 0 1 1-6 0 3 3 0 0 1 6 0z',
    edit: 'm4 20 4-.8L20 7l-3-3L5 16zM14 7l3 3',
    trash: 'M4 6h16M9 6V3h6v3M6 6l1 15h10l1-15M10 10v7m4-7v7',
    left: 'm15 5-7 7 7 7',
    right: 'm9 5 7 7-7 7',
    down: 'm5 9 7 7 7-7',
  };

  compter(type: TypeOffre | ''): number {
    return this.etat.offres().filter((offre) => !type || offre.type === type).length;
  }

  prix(tarif: number): string {
    return nombre.format(tarif) + ' DT / jour';
  }

  dates(offre: OffreProfessionnelle): string {
    if (!offre.debut) return 'Toute l’année';
    return dateCourte.formatRange(new Date(offre.debut), new Date(offre.fin));
  }

  changerMois(delta: number): void {
    const { annee, numero } = this.mois();
    const date = new Date(Date.UTC(annee, numero + delta, 1));
    this.mois.set({ annee: date.getUTCFullYear(), numero: date.getUTCMonth() });
  }

  gererDisponibilites(): void {
    this.joursBrouillon.set([...this.etat.disponibilites()]);
    this.editionCalendrier.set(true);
  }

  basculerJour(date: string): void {
    if (!this.editionCalendrier()) return;
    this.joursBrouillon.update((jours) => jours.includes(date)
      ? jours.filter((jour) => jour !== date)
      : [...jours, date]);
  }

  validerCalendrier(): void {
    this.etat.enregistrerDisponibilites([...this.joursBrouillon()].sort());
    this.editionCalendrier.set(false);
    this.message.set('Disponibilités enregistrées.');
  }

  ouvrir(dialogue: HTMLDialogElement, mode: typeof this.mode, offre?: OffreProfessionnelle): void {
    this.message.set('');
    this.erreur = '';
    this.mode = mode;
    this.selection.set(offre ?? null);
    if (mode === 'publier' || mode === 'modifier') {
      this.formulaire = offre ? { ...offre } : this.nouvelleOffre();
    }
    dialogue.showModal();
  }

  enregistrer(dialogue: HTMLDialogElement): void {
    const { titre, zone, tarif, debut, fin } = this.formulaire;
    if (!titre.trim() || !zone.trim() || tarif === null || !Number.isFinite(tarif) || tarif < 0) {
      this.erreur = 'Renseignez le titre, la zone et un tarif valide.';
      return;
    }
    if (Boolean(debut) !== Boolean(fin) || (debut && fin < debut)) {
      this.erreur = 'Indiquez une période complète et une fin postérieure ou égale au début.';
      return;
    }
    const image = this.formulaire.type === 'Chauffeur' ? '/images/sahara.jpg' : '/images/carthage.jpg';
    this.etat.sauvegarder({
      ...this.formulaire,
      id: this.formulaire.id || crypto.randomUUID(),
      titre: titre.trim(),
      zone: zone.trim(),
      description: this.formulaire.description.trim(),
      tarif,
      image: this.selection()?.type === this.formulaire.type ? this.formulaire.image : image,
    });
    this.message.set(this.mode === 'publier' ? 'Offre publiée.' : 'Offre modifiée.');
    dialogue.close();
  }

  supprimer(dialogue: HTMLDialogElement): void {
    const offre = this.selection();
    if (!offre) return;
    this.etat.supprimer(offre.id);
    if (this.zone() && !this.zones().includes(this.zone())) this.zone.set('');
    this.message.set('Offre supprimée.');
    dialogue.close();
  }

  reinitialiser(): void {
    this.recherche.set('');
    this.type.set('');
    this.zone.set('');
    this.disponibilite.set('');
  }

  private nouvelleOffre(): Omit<OffreProfessionnelle, 'tarif'> & { tarif: number | null } {
    return { id: '', titre: '', type: 'Guide touristique', zone: '', description: '', tarif: null,
      debut: '', fin: '', statut: 'Publiée', image: '' };
  }
}
