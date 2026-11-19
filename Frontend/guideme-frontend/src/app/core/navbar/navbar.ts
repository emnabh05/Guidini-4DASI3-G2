import { Component, HostListener, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

interface NavItem { label: string; children: { label: string; link: string; fragment?: string }[]; }

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {
  scrolled = signal(false);
  menuOpen = signal(false);
  openIndex = signal<number | null>(null);

  items: NavItem[] = [
    { label: 'The Camp', children: [
      { label: 'Notre histoire', link: '/the-camp', fragment: 'notre-histoire' },
      { label: 'L’équipe', link: '/the-camp', fragment: 'equipe' },
      { label: 'Professionnels vérifiés', link: '/the-camp', fragment: 'professionnels-verifies' }] },
    { label: 'Trips', children: [
      { label: 'Circuits', link: '/trips', fragment: 'circuits' },
      { label: 'Week-ends', link: '/trips', fragment: 'week-ends' },
      { label: 'Sur mesure', link: '/trips', fragment: 'sur-mesure' }] },
    { label: 'Accommodation', children: [
      { label: 'Hôtels', link: '/accommodation', fragment: 'hotels' },
      { label: 'Maisons d’hôtes', link: '/accommodation', fragment: 'maisons-d-hotes' },
      { label: 'Camps du désert', link: '/accommodation', fragment: 'camps-du-desert' }] },
    { label: 'Experiences', children: [
      { label: 'Culture', link: '/experiences', fragment: 'culture' },
      { label: 'Gastronomie', link: '/experiences', fragment: 'gastronomie' },
      { label: 'Mer & Plage', link: '/experiences', fragment: 'mer-et-plage' }] },
    { label: 'Activities', children: [
      { label: 'Nature', link: '/activities', fragment: 'nature' },
      { label: 'Aventure', link: '/activities', fragment: 'aventure' },
      { label: 'Sports nautiques', link: '/activities', fragment: 'sports-nautiques' }] },
  ];

  @HostListener('window:scroll')
  onScroll() { this.scrolled.set(window.scrollY > 40); }

  toggle(i: number) { this.openIndex.set(this.openIndex() === i ? null : i); }
  close() { this.openIndex.set(null); this.menuOpen.set(false); }
}
