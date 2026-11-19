import { Component, computed, inject, input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Session } from '../../ms1-users/session';

interface Lien {
  label: string;
  route?: string;
  fragment?: string;
  icone: string;
}

const accueil = 'M3 11l9-8 9 8M5 10v10h5v-6h4v6h5V10';
const missions = 'M3 8h18v12H3zM8 8V5h8v3M3 13h18';
const calendrier = 'M3 5h18v16H3zM3 10h18M8 3v4M16 3v4M7 14h2M11 14h2M15 14h2';

const professionnel: Lien[] = [
  { label: 'Accueil', route: '/professionnel', icone: accueil },
  { label: 'Missions', route: '/missions', icone: missions },
  { label: 'Mes Offres', route: '/professionnel/offres', icone: missions },
  { label: 'Mes Invitations', route: '/professionnel/invitations', icone: missions },
  { label: 'Planning', icone: calendrier },
];

const prestataire: Lien[] = [
  { label: 'Accueil', route: '/prestataire', icone: accueil },
  { label: 'Mes Produits', icone: 'M12 3l8 4.5v9L12 21l-8-4.5v-9zM4 7.5l8 4.5 8-4.5M12 12v9' },
  { label: 'Mes Offres', icone: 'M3 12V4h8l10 10-8 8zM7.5 8.5h.01' },
  { label: 'Mes Missions', route: '/prestataire/missions', icone: missions },
  { label: 'Réservations', route: '/prestataire', fragment: 'reservations', icone: calendrier },
  { label: 'Revenus', icone: 'M4 20V14M4 20h16M9 20V9M14 20V5M19 20V11' },
];

@Component({
  selector: 'app-navbar-espace',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar-espace.html',
  styleUrl: './navbar-espace.scss',
})
export class NavbarEspace {
  private readonly session = inject(Session);
  readonly espace = input<'prestataire' | 'professionnel'>('prestataire');
  readonly invitationsEnAttente = input(0);
  readonly estProfessionnel = computed(() => this.espace() === 'professionnel');
  readonly liens = computed(() => this.estProfessionnel() ? professionnel : prestataire);

  logout(): void {
    this.session.logout();
  }
}
