import { Component, HostListener, inject, signal } from '@angular/core';
import { Session } from '../../../ms1-users/session';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.scss',
})
export class AdminDashboard {
  private readonly session = inject(Session);

  /** Maquette dessinée sur 1672 px de large : mise à l'échelle par `zoom`. */
  zoom = signal(typeof window !== 'undefined' ? window.innerWidth / 1672 : 1);
  @HostListener('window:resize') onResize() { this.zoom.set(window.innerWidth / 1672); }

  menuOpen = signal(false);
  active = signal('Tableau de bord');
  logout() { this.session.logout(); }
  @HostListener('document:click', ['$event']) onDoc(e: Event) {
    if (!(e.target as Element).closest('.um')) this.menuOpen.set(false);
  }

  nav = [
    { label: 'Tableau de bord', y: 127, tx: 86, paths: ['M3 11l9-8 9 8', 'M5 10v11h5v-7h4v7h5V10'] },
    { label: 'Utilisateurs', y: 186, tx: 87, chev: true, paths: ['M9 11a4 4 0 100-8 4 4 0 000 8', 'M2 21c0-8 14-8 14 0', 'M17 4a4 4 0 010 7', 'M18 15c3 1 4 3 4 6'] },
    { label: 'Produits & Activités', y: 238, tx: 88, chev: true, paths: ['M12 3l8 4.5v9L12 21l-8-4.5v-9z', 'M4 7.5l8 4.5 8-4.5M12 12v9'] },
    { label: 'Offres', y: 291, tx: 91, paths: ['M3 12V4h8l10 10-8 8z', 'M7.5 8.5h.01'] },
    { label: 'Missions', y: 343, tx: 92, paths: ['M3 8h18v12H3z', 'M8 8V5h8v3', 'M3 13h18'] },
    { label: 'Réservations', y: 394, tx: 93, paths: ['M3 5h18v16H3z', 'M3 10h18M8 3v4M16 3v4M7 14h2M11 14h2M15 14h2M7 18h2M11 18h2'] },
    { label: 'Réclamations', y: 445, tx: 93, paths: ['M12 3L2 21h20z', 'M12 10v5M12 18h.01'] },
    { label: 'Statistiques', y: 513, tx: 94, paths: ['M4 20V14M4 20h16M9 20V9M14 20V5M19 20V11'] },
    { label: 'Paramètres', y: 562, tx: 95, paths: ['M12 15a3 3 0 100-6 3 3 0 000 6z', 'M19.4 15a1.7 1.7 0 00.3 1.9l.1.1a2 2 0 11-2.8 2.8l-.1-.1a1.7 1.7 0 00-1.9-.3 1.7 1.7 0 00-1 1.5V21a2 2 0 11-4 0v-.1a1.7 1.7 0 00-1.1-1.5 1.7 1.7 0 00-1.9.3l-.1.1a2 2 0 11-2.8-2.8l.1-.1a1.7 1.7 0 00.3-1.9 1.7 1.7 0 00-1.5-1H3a2 2 0 110-4h.1a1.7 1.7 0 001.5-1.1 1.7 1.7 0 00-.3-1.9l-.1-.1a2 2 0 112.8-2.8l.1.1a1.7 1.7 0 001.9.3H9a1.7 1.7 0 001-1.5V3a2 2 0 114 0v.1a1.7 1.7 0 001 1.5 1.7 1.7 0 001.9-.3l.1-.1a2 2 0 112.8 2.8l-.1.1a1.7 1.7 0 00-.3 1.9V9a1.7 1.7 0 001.5 1H21a2 2 0 110 4h-.1a1.7 1.7 0 00-1.5 1z'] },
  ];

  kpis = [
    { x: 301, w: 326, icon: 14, val: 115, badge: 238, value: '2 840', label: 'Utilisateurs', trend: '+12%', cls: 'blue' },
    { x: 643, w: 323, icon: 13, val: 114, badge: 235, value: '356', label: 'Produits & Activités', trend: '+8%', cls: 'orange' },
    { x: 982, w: 324, icon: 13, val: 112, badge: 234, value: '1 245', label: 'Réservations', trend: '+15%', cls: 'purple' },
    { x: 1322, w: 336, icon: 15, val: 115, badge: 237, value: '52', label: 'Missions', trend: '+20%', cls: 'green' },
  ];

  /* Graphique : coordonnées locales à la carte (578 × 266) */
  yTicks = [['1 250', 76], ['1 000', 109], ['750', 141], ['500', 171], ['250', 200], ['0', 228]] as [string, number][];
  months = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin'];
  points = [[114, 197], [199, 169], [284, 173], [369, 144], [453, 142], [542, 111]];
  get linePath() { return this.smooth(this.points); }
  get areaPath() { return `${this.linePath} L542 228 L114 228 Z`; }
  private smooth(p: number[][]) {
    let d = `M${p[0][0]} ${p[0][1]}`;
    for (let i = 0; i < p.length - 1; i++) {
      const p0 = p[i - 1] ?? p[i], p1 = p[i], p2 = p[i + 1], p3 = p[i + 2] ?? p2;
      d += ` C${p1[0] + (p2[0] - p0[0]) / 6} ${p1[1] + (p2[1] - p0[1]) / 6} ${p2[0] - (p3[0] - p1[0]) / 6} ${p2[1] - (p3[1] - p1[1]) / 6} ${p2[0]} ${p2[1]}`;
    }
    return d;
  }

  legend = [
    { label: 'Activités (48%)', color: '#1a7cff' },
    { label: 'Circuits (27%)', color: '#ff9500' },
    { label: 'Offres (17%)', color: '#8b5cf6' },
    { label: 'Autres (8%)', color: '#10c98f' },
  ];

  demandes = [
    { label: 'Produit à valider', count: 12 },
    { label: 'Mission à valider', count: 4 },
    { label: 'Réclamation non traitée', count: 3 },
  ];

  validations = [
    { title: 'Visite de Carthage', sub: 'Produit · Soumis il y a 2 h' },
    { title: 'Circuit Sahara – 3 jours', sub: 'Offre · Soumis il y a 4 h' },
    { title: 'Excursion Sidi Bou Saïd', sub: 'Produit · Soumis il y a 6 h' },
  ];

  actions = [
    { label: ['Valider', 'un produit'], primary: true, x: 22, w: 145, paths: ['M12 3l8 3v6c0 5-3.5 8-8 9-4.5-1-8-4-8-9V6z', 'M8.5 12l2.5 2.5 4.5-5'] },
    { label: ['Gérer', 'les missions'], x: 181, w: 142, paths: ['M3 8h18v12H3z', 'M8 8V5h8v3', 'M3 13h18'] },
    { label: ['Gérer', 'les utilisateurs'], x: 338, w: 145, paths: ['M9 11a4 4 0 100-8 4 4 0 000 8', 'M2 21c0-8 14-8 14 0', 'M17 4a4 4 0 010 7', 'M18 15c3 1 4 3 4 6'] },
    { label: ['Consulter', 'les rapports'], x: 497, w: 144, paths: ['M4 20V14M4 20h16M9 20V9M14 20V5M19 20V11'] },
  ];
}
