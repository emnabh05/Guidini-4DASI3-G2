import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AdminNavigation } from './admin-navigation';
@Component({
  selector: 'app-admin-accueil',
  imports: [AdminNavigation, FormsModule],
  templateUrl: './admin-accueil.html',
  styleUrl: './admin-accueil.scss',
})
export class AdminAccueil {
  period = signal('6m');
  expanded = signal(false);
  query = signal('');
  modal = signal('');
  notice = signal('');
  draft = '';
  nav = [
    { label: 'Accueil', icon: 'M3 11l9-8 9 8M5 10v11h5v-7h4v7h5V10' },
    {
      label: 'Utilisateurs',
      icon: 'M9 11a4 4 0 100-8 4 4 0 000 8M2 21c0-8 14-8 14 0M17 4a4 4 0 010 7M18 15c3 1 4 3 4 6',
    },
    {
      label: 'Produits & Offres',
      icon: 'M12 2l10 5v10l-10 5-10-5V7zM2 7l10 5 10-5M12 12v10M7 5l10 5M7 12v3M17 13v3',
    },
    {
      label: 'Réservations',
      icon: 'M4 5h16v16H4zM4 10h16M8 3v4M16 3v4M8 14h2M14 14h2M8 18h2M14 18h2',
    },
    { label: 'Missions', icon: 'M3 7h18v14H3zM8 7V3h8v4M3 12h6l1 3h4l1-3h6' },
    { label: 'Réclamations', icon: 'M12 3L2 21h20zM12 9v5M12 18h.01' },
    { label: 'Rapports & Statistiques', icon: 'M3 21V11h4v10M10 21V6h4v15M17 21V2h4v19' },
    {
      label: 'Paramètres',
      icon: 'M12 8a4 4 0 100 8 4 4 0 000-8M9 3h6l1 3 3 1 2 5-2 5-3 1-1 3H9l-1-3-3-1-2-5 2-5 3-1z',
    },
  ];
  stats = [
    { label: 'Utilisateurs', value: '2 840', delta: '+12%', tone: 'blue', icon: this.nav[1].icon },
    {
      label: 'Produits & Offres',
      value: '356',
      delta: '+8%',
      tone: 'orange',
      icon: this.nav[2].icon,
    },
    {
      label: 'Réservations',
      value: '1 245',
      delta: '+15%',
      tone: 'purple',
      icon: this.nav[3].icon,
    },
    { label: 'Réclamations', value: '32', delta: '+3%', tone: 'pink', icon: this.nav[5].icon },
  ];
  categories = [
    { name: 'Activités', value: 48, color: '#0084ff' },
    { name: 'Circuits', value: 27, color: '#913cff' },
    { name: 'Offres', value: 17, color: '#ff9800' },
    { name: 'Autres', value: 8, color: '#35d66b' },
  ];
  quick = [
    { label: 'Ajouter un produit', icon: 'M12 3v18M3 12h18' },
    {
      label: 'Créer un utilisateur',
      icon: 'M9 11a4 4 0 100-8 4 4 0 000 8M2 21c0-8 14-8 14 0M19 8v8M15 12h8',
    },
    { label: 'Valider des offres', icon: 'M8 4H4v18h16V4h-4M8 2h8v5H8zM8 13l3 3 5-6' },
    { label: 'Générer un rapport', icon: this.nav[6].icon },
  ];
  services = [
    'Utilisateurs',
    'Produits',
    'Réservations',
    'Missions',
    'IA',
    'Publications',
    'Réclamations',
  ];
  activities = [
    {
      text: 'Nouvel utilisateur inscrit : sarra.benali@gmail.com',
      time: 'Il y a 5 min',
      icon: this.nav[1].icon,
      color: '#0084ff',
    },
    {
      text: 'Produit soumis à validation : Visite de Carthage',
      time: 'Il y a 12 min',
      icon: this.nav[2].icon,
      color: '#0062ba',
    },
    {
      text: 'Nouvelle réservation : #RES-4587',
      time: 'Il y a 20 min',
      icon: this.nav[3].icon,
      color: '#1225ff',
    },
    {
      text: 'Réclamation créée : #REC-032',
      time: 'Il y a 27 min',
      icon: this.nav[5].icon,
      color: '#ff7000',
    },
    {
      text: 'Offre mise à jour : Circuit Sahara',
      time: 'Il y a 45 min',
      icon: this.nav[2].icon,
      color: '#0062ba',
    },
  ];
  offers = ['Visite de Carthage', 'Circuit Sahara · 3 jours', 'Randonnée à Aïn Draham'];
  get filteredActivities() {
    const q = this.query().trim().toLocaleLowerCase('fr');
    return this.activities.filter((a) => a.text.toLocaleLowerCase('fr').includes(q));
  }
  get values() {
    return this.period() === '6m'
      ? [260, 500, 470, 700, 715, 960]
      : [260, 500, 470, 700, 715, 960, 810, 1050, 890, 1000, 1120, 1245];
  }
  get months() {
    return [
      'Jan',
      'Fév',
      'Mar',
      'Avr',
      'Mai',
      'Juin',
      'Juil',
      'Août',
      'Sep',
      'Oct',
      'Nov',
      'Déc',
    ].slice(0, this.values.length);
  }
  get points() {
    return this.values.map((v, i) => ({
      x: 55 + (i * 460) / (this.values.length - 1),
      y: 190 - (v / 1250) * 150,
      value: v,
    }));
  }
  get line() {
    const p = this.points;
    return p
      .map((v, i) =>
        i ? `C${p[i - 1].x + 30},${p[i - 1].y} ${v.x - 30},${v.y} ${v.x},${v.y}` : `M${v.x},${v.y}`,
      )
      .join(' ');
  }
  get area() {
    return `${this.line} L515,190 L55,190 Z`;
  }
  open(label: string) {
    this.draft = '';
    if (label === 'Générer un rapport') {
      const csv = 'Indicateur;Valeur\n' + this.stats.map((s) => `${s.label};${s.value}`).join('\n');
      const url = URL.createObjectURL(
        new Blob(['\ufeff' + csv], { type: 'text/csv;charset=utf-8' }),
      );
      const a = document.createElement('a');
      a.href = url;
      a.download = 'guidini-rapport-demo.csv';
      a.click();
      URL.revokeObjectURL(url);
      this.notice.set('Rapport de démonstration téléchargé.');
      return;
    }
    this.modal.set(label);
  }
  save() {
    const value = this.draft.trim();
    if (!value) return;
    this.activities.unshift({
      text: `${this.modal() === 'Ajouter un produit' ? 'Produit soumis à validation' : 'Nouvel utilisateur inscrit'} : ${value}`,
      time: 'À l’instant',
      icon: this.nav[1].icon,
      color: '#0084ff',
    });
    this.notice.set('Ajout effectué dans la démonstration locale.');
    this.modal.set('');
  }
  approve(offer: string) {
    this.offers = this.offers.filter((o) => o !== offer);
    this.activities.unshift({
      text: `Offre validée : ${offer}`,
      time: 'À l’instant',
      icon: this.nav[2].icon,
      color: '#0084ff',
    });
  }
}
