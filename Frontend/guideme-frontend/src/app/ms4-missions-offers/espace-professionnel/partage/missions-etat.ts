import { Injectable, computed, signal } from '@angular/core';

type Reponse = { statut: 'Acceptée' | 'Refusée'; tarif?: number };

const missions = [
  { id: 1, titre: 'Visite de Carthage', titreInvitation: 'Visite de Carthage', lieu: 'Tunis, Carthage', dates: '20 – 22 Déc 2026', mois: '2026-12', type: 'Guide touristique', effectif: '1 guide', tarif: 400, tarifInvitation: 400, image: '/images/carthage.jpg' },
  { id: 2, titre: 'Circuit Sidi Bou Saïd', titreInvitation: 'Sidi Bou Saïd', lieu: 'Sidi Bou Saïd', dates: '15 – 18 Déc 2026', mois: '2026-12', type: 'Guide touristique', effectif: '1 guide', tarif: 350, tarifInvitation: 350, image: '/admin/val3.jpg' },
  { id: 3, titre: 'Excursion Sahara – 3 jours', titreInvitation: 'Excursion Sahara', lieu: 'Tozeur, Douz', dates: '02 – 05 Jan 2027', mois: '2027-01', type: 'Chauffeur', effectif: '1 chauffeur', tarif: 1200, tarifInvitation: 1100, image: '/images/sahara.jpg' },
];

type Mission = (typeof missions)[number];

function normaliser(texte: string): string {
  return texte.normalize('NFD').replace(/\p{Diacritic}/gu, '').toLowerCase().trim();
}


export const IconesProfessionnelles: Record<string, string> = {
    home: 'M3 11l9-8 9 8M5 10v10h5v-6h4v6h5V10',
    brief: 'M3 8h18v12H3zM8 8V5h8v3M3 13h18M11 13v2h2v-2',
    calendar: 'M3 5h18v16H3zM3 10h18M8 3v4M16 3v4M8 14h2m4 0h2m-8 3h2',
    bell: 'M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9m-8 12a2 2 0 0 0 4 0',
    mail: 'M3 5h18v14H3zM3 6l9 7 9-7',
    chevron: 'm6 9 6 6 6-6',
    search: 'M15 15l5 5M17 10a7 7 0 1 1-14 0 7 7 0 0 1 14 0z',
    pin: 'M20 10c0 5-8 11-8 11S4 15 4 10a8 8 0 1 1 16 0zm-5 0a3 3 0 1 1-6 0 3 3 0 0 1 6 0z',
    filter: 'M3 4h18l-7 8v6l-4 2v-8z',
    people: 'M15 20v-2a4 4 0 0 0-4-4H7a4 4 0 0 0-4 4v2m18 0v-2a4 4 0 0 0-3-4M12 7a3 3 0 1 1-6 0 3 3 0 0 1 6 0zm5-3a3 3 0 0 1 0 6',
    car: 'M4 11l2-5h12l2 5v7H4zM4 14h16M5 18v2m14-2v2M8 16h.01M16 16h.01',
    heart: 'M20.8 5.5a5.3 5.3 0 0 0-7.5 0L12 6.8l-1.3-1.3a5.3 5.3 0 0 0-7.5 7.5l1.3 1.3L12 21l7.5-6.7 1.3-1.3a5.3 5.3 0 0 0 0-7.5z',
    send: 'm22 2-7 20-4-9-9-4 20-7zM22 2 11 13',
    check: 'm5 12 4 4L19 6',
    close: 'm6 6 12 12M18 6 6 18',
    arrow: 'M4 12h16m-6-6 6 6-6 6',
    star: 'm12 2 3 6.7 7 .9-5 5 1.2 7.1-6.2-3.4L5.8 22 7 14.6 2 9.6l7-.9z',
    person: 'M16 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0zM4 22v-2a8 8 0 0 1 16 0v2',
  };

@Injectable()
export class MissionsEtat {
  private readonly formatNombre = new Intl.NumberFormat('fr-TN');
  readonly missions = missions;
  readonly favoris = signal<ReadonlySet<number>>(new Set());
  readonly candidatures = signal<ReadonlySet<number>>(new Set());
  readonly reponses = signal<Partial<Record<number, Reponse>>>({});
  readonly enAttente = computed(() => missions.filter(({ id }) => !this.reponses()[id]).length);
  readonly message = signal('');
  readonly filtres = signal({ recherche: '', lieu: '', periode: '', type: '' });
  readonly resultats = computed(() => {
    const { recherche, lieu, periode, type } = this.filtres();
    return missions.filter((mission) =>
      normaliser(mission.titre + ' ' + mission.lieu).includes(normaliser(recherche)) &&
      (!lieu || mission.lieu === lieu) &&
      (!periode || mission.mois === periode) &&
      (!type || mission.type === type),
    );
  });

  readonly tarifPropose = signal<number | null>(null);
  readonly action = signal<{ mission: Mission; type: 'postuler' | 'accepter' | 'details' } | null>(null);

  basculerFavori(id: number): void {
    const suivants = new Set(this.favoris());
    if (!suivants.delete(id)) suivants.add(id);
    this.favoris.set(suivants);
  }

  ouvrir(mission: Mission, type: 'postuler' | 'accepter' | 'details'): void {
    this.action.set({ mission, type });
    this.tarifPropose.set(mission.tarifInvitation);
  }

  confirmer(): void {
    const action = this.action();
    if (!action || action.type === 'details') return;

    if (action.type === 'accepter') {
      const tarif = this.tarifPropose();
      if (tarif === null || !Number.isFinite(tarif) || tarif < 0) return;
      this.reponses.update((actuelles) => ({ ...actuelles, [action.mission.id]: { statut: 'Acceptée', tarif } }));
      this.message.set('Réponse enregistrée dans la démo.');
    } else {
      this.candidatures.update((actuelles) => new Set([...actuelles, action.mission.id]));
      this.message.set('Candidature enregistrée dans la démo.');
    }
    this.action.set(null);
  }

  refuser(id: number): void {
    this.reponses.update((actuelles) => ({ ...actuelles, [id]: { statut: 'Refusée' } }));
    this.message.set('Invitation refusée dans la démo.');
  }

  montant(valeur: number): string {
    return this.formatNombre.format(valeur) + ' DT';
  }

}
