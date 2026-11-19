import { computed, Injectable, signal } from '@angular/core';

export type TypeOffre = 'Guide touristique' | 'Chauffeur';
export type StatutOffre = 'Publiée' | 'En attente';

export interface OffreProfessionnelle {
  id: string;
  titre: string;
  type: TypeOffre;
  zone: string;
  description: string;
  tarif: number;
  debut: string;
  fin: string;
  statut: StatutOffre;
  image: string;
}

const offresInitiales: OffreProfessionnelle[] = [
  { id: 'carthage', titre: 'Guide culturel certifié', type: 'Guide touristique', zone: 'Tunis, Carthage et environs', description: 'Visites culturelles, historiques et découverte', tarif: 120, debut: '2026-12-20', fin: '2026-12-31', statut: 'Publiée', image: '/images/carthage.jpg' },
  { id: 'sidi', titre: 'Guide accompagnateur', type: 'Guide touristique', zone: 'Sidi Bou Saïd, La Marsa, Carthage', description: 'Accompagnement de groupes et circuits à pied', tarif: 100, debut: '2026-12-15', fin: '2026-12-25', statut: 'En attente', image: '/admin/val3.jpg' },
  { id: 'transport', titre: 'Service de transport touristique', type: 'Chauffeur', zone: 'Tunis et environs', description: 'Transport des voyageurs et transferts', tarif: 350, debut: '', fin: '', statut: 'Publiée', image: '/images/sahara.jpg' },
];

function charger<T>(cle: string, defaut: T): T {
  try {
    const valeur = localStorage.getItem(cle);
    return valeur === null ? defaut : JSON.parse(valeur) as T;
  } catch {
    return defaut;
  }
}

function enregistrer<T>(cle: string, valeur: T): void {
  try {
    localStorage.setItem(cle, JSON.stringify(valeur));
  } catch {
    // Le signal conserve les données pendant la session.
  }
}

@Injectable({ providedIn: 'root' })
export class OffresEtat {
  private readonly cleOffres = 'guidini.ms4.offres.demo';
  private readonly cleJours = 'guidini.ms4.disponibilites.demo';
  private readonly liste = signal(charger(this.cleOffres, offresInitiales));
  private readonly jours = signal(charger(this.cleJours,
    [20, 21, 22, 23, 28, 29, 30, 31].map((jour) => '2026-12-' + jour)));
  readonly offres = this.liste.asReadonly();
  readonly disponibilites = this.jours.asReadonly();
  readonly publiees = computed(() => this.offres().filter(({ statut }) => statut === 'Publiée'));

  sauvegarder(offre: OffreProfessionnelle): void {
    this.liste.update((liste) => liste.some(({ id }) => id === offre.id)
      ? liste.map((existante) => existante.id === offre.id ? offre : existante)
      : [offre, ...liste]);
    enregistrer(this.cleOffres, this.offres());
  }

  supprimer(id: string): void {
    this.liste.update((liste) => liste.filter((offre) => offre.id !== id));
    enregistrer(this.cleOffres, this.offres());
  }

  enregistrerDisponibilites(jours: string[]): void {
    this.jours.set(jours);
    enregistrer(this.cleJours, jours);
  }
}
