import { Component, inject, ElementRef, HostListener, ViewChild, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Session } from '../../ms1-users/session';

interface Icon { paths: string[]; }

@Component({
  selector: 'app-touriste',
  imports: [FormsModule, RouterLink],
  templateUrl: './touriste.html',
  styleUrl: './touriste.scss',
})
export class Touriste {
  private readonly session = inject(Session);
  menuOpen = signal(false);
  logout() { this.session.logout(); }
  @HostListener('document:click', ['$event']) onDoc(e: Event) {
    if (!(e.target as Element).closest('.um')) this.menuOpen.set(false);
  }

  @ViewChild('track') track!: ElementRef<HTMLElement>;

  /** La maquette est dessinée sur 1672 px de large : on met tout à l'échelle. */
  zoom = signal(typeof window !== 'undefined' ? window.innerWidth / 1672 : 1);
  @HostListener('window:resize') onResize() { this.zoom.set(window.innerWidth / 1672); }

  destination = '';
  date = '';
  travelers: number | null = null;
  dateType = 'text';
  activeCat = signal<string | null>(null);
  favorites = signal<Set<string>>(new Set());

  user = { name: 'Sarra B.', role: 'Touriste', avatar: '/touriste/avatar.jpg' };

  nav = [
    { label: 'Accueil', link: '/touriste', active: true, paths: ['M3 11l9-8 9 8', 'M5 10v11h5v-7h4v7h5V10'] },
    { label: 'Activités', link: '/touriste', paths: ['M12 3L2 21h20z', 'M9 21l3-6 3 6'] },
    { label: 'Circuits', link: '/touriste', paths: ['M6 4a2 2 0 100 4 2 2 0 000-4', 'M18 16a2 2 0 100 4 2 2 0 000-4', 'M6 8c0 6 12 2 12 8'] },
    { label: 'Offres', link: '/touriste', paths: ['M3 12V4h8l10 10-8 8z', 'M7.5 8.5h.01'] },
    { label: 'Communauté', link: '/touriste', paths: ['M9 11a3 3 0 100-6 3 3 0 000 6', 'M3 20c0-5 12-5 12 0', 'M17 11a3 3 0 100-6', 'M18 14c3 .5 4 3 4 6'] },
  ];

  categories: { label: string; color: string; paths: string[] }[] = [
    { label: 'Culture', color: '#4b5d7a', paths: ['M3 9l9-6 9 6', 'M5 9v9M9 9v9M15 9v9M19 9v9', 'M3 21h18'] },
    { label: 'Nature', color: '#2ea44f', paths: ['M5 19C4 11 9 5 20 4c0 11-6 16-14 15z', 'M5 19c3-5 6-8 10-10'] },
    { label: 'Aventure', color: '#f26a1b', paths: ['M13 4a1.5 1.5 0 100 3', 'M9 21l2-6-3-3 3-4 4 3 3 1', 'M11 11l-3 2'] },
    { label: 'Mer & Plage', color: '#1a56db', paths: ['M3 17c3 0 3-2 6-2s3 2 6 2 3-2 6-2', 'M4 12C4 7 9 4 13 5c-3 1-4 4-3 7'] },
    { label: 'Gastronomie', color: '#d9570d', paths: ['M6 3v8a2 2 0 004 0V3', 'M8 11v10', 'M17 3c-2 2-2 6 0 8v10'] },
    { label: 'Excursions', color: '#1a56db', paths: ['M3 15V11l2-5h14l2 5v4', 'M3 15h18v3H3z', 'M7 18v2M17 18v2'] },
  ];

  experiences = [
    { title: 'Visite de Carthage', cat: 'Culture', duration: '½ journée', price: 45, rating: 4.8, reviews: 320,
      img: '/touriste/carthage.jpg' },
    { title: 'Découverte de Sidi Bou Saïd', cat: 'Culture', duration: '1 journée', price: 60, rating: 4.7, reviews: 280,
      img: '/touriste/sidi.jpg' },
    { title: 'Aventure dans le Sahara', cat: 'Aventure', duration: '3 jours', price: 350, rating: 4.9, reviews: 410, multi: true,
      img: '/touriste/sahara.jpg' },
    { title: 'Randonnée à Aïn Draham', cat: 'Nature', duration: '1 journée', price: 70, rating: 4.6, reviews: 190,
      img: '/touriste/aindraham.jpg' },
  ];

  trips = [
    { title: 'Visite de Carthage', date: '12 Déc 2026', people: 2, status: 'Confirmée',
      img: '/touriste/trip.jpg' },
  ];

  trust = [
    { label: 'Réservation sécurisée', paths: ['M12 3l7 3v5c0 5-3 8.5-7 10-4-1.5-7-5-7-10V6z', 'M8.5 12l2.5 2.5L15.5 10'] },
    { label: 'Support 24/7', paths: ['M4 14v-2a8 8 0 0116 0v2', 'M4 13h2.5v5H5a1 1 0 01-1-1zM20 13h-2.5v5H19a1 1 0 001-1z', 'M18 18c0 2-2 3-5 3'] },
    { label: 'Prestataires vérifiés', paths: ['M12 2.5l2.2 1.6 2.7-.2 1 2.5 2.3 1.5-.8 2.6.8 2.6-2.3 1.5-1 2.5-2.7-.2L12 21.5l-2.2-1.6-2.7.2-1-2.5-2.3-1.5.8-2.6-.8-2.6 2.3-1.5 1-2.5 2.7.2z', 'M12 8.5l1.1 2.3 2.5.3-1.8 1.7.5 2.5-2.3-1.2-2.3 1.2.5-2.5-1.8-1.7 2.5-.3z'] },
    { label: 'Expériences authentiques', paths: ['M5 19C4 11 9 5 20 4c0 11-6 16-14 15z', 'M5 19c3-5 6-8 10-10'] },
  ];

  get filtered() {
    const c = this.activeCat();
    const q = this.destination.trim().toLowerCase();
    return this.experiences.filter(
      (e) => (!c || e.cat === c) && (!q || e.title.toLowerCase().includes(q)),
    );
  }

  pickCat(label: string) { this.activeCat.set(this.activeCat() === label ? null : label); }

  toggleFav(title: string) {
    const s = new Set(this.favorites());
    s.has(title) ? s.delete(title) : s.add(title);
    this.favorites.set(s);
  }

  scroll(dir: number) {
    const el = this.track.nativeElement;
    el.scrollBy({ left: dir * (el.clientWidth * 0.8), behavior: 'smooth' });
  }

  search() {
    // TODO: brancher sur le microservice produits/offres
    document.getElementById('recommandes')?.scrollIntoView({ behavior: 'smooth' });
  }
}
