import { Component, inject, HostListener, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Session } from '../../ms1-users/session';

@Component({
  selector: 'app-prestataire',
  imports: [RouterLink],
  templateUrl: './prestataire.html',
  styleUrl: './prestataire.scss',
})
export class Prestataire {
  private readonly session = inject(Session);
  menuOpen = signal(false);
  logout() { this.session.logout(); }
  @HostListener('document:click', ['$event']) onDoc(e: Event) {
    if (!(e.target as Element).closest('.um')) this.menuOpen.set(false);
  }

  /** Maquette dessinée sur 1672 px de large : mise à l'échelle par `zoom`. */
  zoom = signal(typeof window !== 'undefined' ? window.innerWidth / 1672 : 1);
  @HostListener('window:resize') onResize() { this.zoom.set(window.innerWidth / 1672); }

  agency = 'Agence Tunivoyages';

  nav = [
    { label: 'Accueil', active: true, paths: ['M3 11l9-8 9 8', 'M5 10v11h5v-7h4v7h5V10'] },
    { label: 'Mes Produits', paths: ['M12 3l8 4.5v9L12 21l-8-4.5v-9z', 'M4 7.5l8 4.5 8-4.5M12 12v9'] },
    { label: 'Mes Offres', paths: ['M3 12V4h8l10 10-8 8z', 'M7.5 8.5h.01'] },
    { label: 'Mes Missions', paths: ['M3 8h18v12H3z', 'M8 8V5h8v3', 'M3 13h18'] },
    { label: 'Réservations', paths: ['M3 5h18v16H3z', 'M3 10h18M8 3v4M16 3v4M7 14h2M11 14h2M15 14h2M7 18h2M11 18h2'] },
    { label: 'Revenus', paths: ['M4 20V14M4 20h16M9 20V9M14 20V5M19 20V11'] },
  ];

  kpis = [
    { x: 35, w: 320, icon: 18, val: 108, badge: 227, value: '24', label: 'Réservations', trend: '+20%', cls: 'blue' },
    { x: 374, w: 298, icon: 18, val: 109, badge: 206, value: '18', label: 'Produits', trend: '+12%', cls: 'orange' },
    { x: 690, w: 298, icon: 18, val: 110, badge: 207, value: '6', label: 'Offres', trend: '+8%', cls: 'purple' },
    { x: 1009, w: 309, icon: 19, val: 109, badge: 211, value: '5', label: 'Missions', trend: '+25%', cls: 'blue' },
    { x: 1337, w: 306, icon: 18, val: 116, badge: 0, value: '12 450 DT', label: 'Revenus', trend: '', cls: 'orange' },
  ];

  missions = [
    { title: 'Chauffeur – Circuit Carthage', date: '20 – 22 Déc 2026', status: 'En cours', cls: 'run' },
    { title: 'Guide touristique – Sidi Bou Saïd', date: '15 – 18 Déc 2026', status: 'En attente', cls: 'wait' },
  ];

  actions = [
    { label: ['Ajouter', 'un produit'], primary: true, paths: ['M12 3v18M3 12h18'] },
    { label: ['Créer', 'une offre'], paths: ['M12 3l9 5-9 5-9-5z', 'M3 12.5l9 5 9-5', 'M3 17l9 5 9-5'] },
    { label: ['Publier', 'une mission'], paths: ['M3 8h18v12H3z', 'M8 8V5h8v3', 'M3 13h18M11 13v2h2v-2'] },
    { label: ['Consulter', 'mes réservations'], paths: ['M3 5h18v16H3z', 'M3 10h18M8 3v4M16 3v4M7 14h2M11 14h2M15 14h2M7 18h2M11 18h2'] },
  ];

  reservations = [
    { title: 'Visite de Carthage', date: '12 Déc 2026', people: '2 personnes', price: '90 DT', status: 'Confirmée', cls: 'ok', img: '/prestataire/res1.jpg' },
    { title: 'Circuit Sahara – 3 jours', date: '20 Déc 2026', people: '1 personne', price: '350 DT', status: 'En attente', cls: 'wait', img: '/prestataire/res2.jpg' },
  ];

  legend = [
    { label: 'Activités (50%)', color: '#1a7cff' },
    { label: 'Circuits (28%)', color: '#ff9500' },
    { label: 'Offres (22%)', color: '#8b5cf6' },
  ];

  /* ---- Graphique : coordonnées locales à la carte (672 × 270) ---- */
  yTicks = [30, 25, 20, 15, 10, 5, 0].map((v, i) => ({ v, y: 76 + i * 25.5 }));
  months = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin'];
  points = [[113, 204], [218, 168], [318, 180], [416, 129], [519, 140], [624, 103]];

  get linePath() { return this.smooth(this.points); }
  get areaPath() { return `${this.linePath} L624 229 L113 229 Z`; }

  private smooth(p: number[][]) {
    let d = `M${p[0][0]} ${p[0][1]}`;
    for (let i = 0; i < p.length - 1; i++) {
      const p0 = p[i - 1] ?? p[i], p1 = p[i], p2 = p[i + 1], p3 = p[i + 2] ?? p2;
      const c1x = p1[0] + (p2[0] - p0[0]) / 6, c1y = p1[1] + (p2[1] - p0[1]) / 6;
      const c2x = p2[0] - (p3[0] - p1[0]) / 6, c2y = p2[1] - (p3[1] - p1[1]) / 6;
      d += ` C${c1x} ${c1y} ${c2x} ${c2y} ${p2[0]} ${p2[1]}`;
    }
    return d;
  }
}
