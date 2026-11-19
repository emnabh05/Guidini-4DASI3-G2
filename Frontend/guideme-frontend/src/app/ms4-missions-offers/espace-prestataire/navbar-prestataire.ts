import { Component, HostListener, inject, input, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Session } from '../../ms1-users/session';

@Component({
  selector: 'app-navbar-prestataire',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar-prestataire.html',
  styleUrl: './navbar-prestataire.scss',
})
export class NavbarPrestataire {
  private readonly session = inject(Session);
  readonly fixe = input(false);
  readonly menuOpen = signal(false);
  readonly agency = 'Agence Tunivoyages';

  logout(): void {
    this.session.logout();
  }

  @HostListener('document:click', ['$event'])
  fermerMenu(event: Event): void {
    if (event.target instanceof Element && !event.target.closest('.um')) this.menuOpen.set(false);
  }

  readonly nav = [
    { label: 'Accueil', route: '/prestataire', paths: ['M3 11l9-8 9 8', 'M5 10v11h5v-7h4v7h5V10'] },
    { label: 'Mes Produits', href: '#', paths: ['M12 3l8 4.5v9L12 21l-8-4.5v-9z', 'M4 7.5l8 4.5 8-4.5M12 12v9'] },
    { label: 'Mes Offres', href: '#', paths: ['M3 12V4h8l10 10-8 8z', 'M7.5 8.5h.01'] },
    { label: 'Mes Missions', route: '/prestataire/missions', paths: ['M3 8h18v12H3z', 'M8 8V5h8v3', 'M3 13h18'] },
    { label: 'Réservations', route: '/prestataire', fragment: 'reservations', paths: ['M3 5h18v16H3z', 'M3 10h18M8 3v4M16 3v4M7 14h2M11 14h2M15 14h2M7 18h2M11 18h2'] },
    { label: 'Revenus', href: '#', paths: ['M4 20V14M4 20h16M9 20V9M14 20V5M19 20V11'] },
  ];
}
