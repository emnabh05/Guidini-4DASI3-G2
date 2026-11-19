import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-accueil',
  imports: [FormsModule],
  templateUrl: './accueil.html',
  styleUrl: './accueil.scss',
})
export class Accueil {
  destination = '';
  date = '';
  travelers: number | null = null;
  dateType = 'text';

  categories = [
    { icon: '🏛️', label: 'Culture' },
    { icon: '🌿', label: 'Nature' },
    { icon: '🏃', label: 'Aventure' },
    { icon: '🌊', label: 'Mer & Plage' },
    { icon: '🍴', label: 'Gastronomie' },
  ];

  experiences = [
    { title: 'Visite de Carthage', cat: 'Culture', duration: '1/2 journée', price: 45, rating: 4.8, reviews: 320,
      img: 'https://images.unsplash.com/photo-1590059530466-2d3f29d3f9ea?w=800&q=80' },
    { title: 'Découverte de Sidi Bou Saïd', cat: 'Culture', duration: '1 journée', price: 60, rating: 4.7, reviews: 280,
      img: 'https://images.unsplash.com/photo-1553603227-2358aabe821e?w=800&q=80' },
    { title: 'Aventure dans le Sahara', cat: 'Aventure', duration: '3 jours', price: 350, rating: 4.9, reviews: 410,
      img: 'https://images.unsplash.com/photo-1509316785289-025f5b846b35?w=800&q=80' },
    { title: 'Randonnée à Aïn Draham', cat: 'Nature', duration: '1 journée', price: 70, rating: 4.6, reviews: 190,
      img: 'https://images.unsplash.com/photo-1448375240586-882707db888b?w=800&q=80' },
  ];

  trust = [
    { label: 'Réservation sécurisée', paths: ['M12 3l7 3v5c0 5-3 8.5-7 10-4-1.5-7-5-7-10V6z', 'M8.5 12l2.5 2.5L15.5 10'] },
    { label: 'Support 24/7', paths: ['M4 14v-2a8 8 0 0116 0v2', 'M4 13h2.5v5H5a1 1 0 01-1-1zM20 13h-2.5v5H19a1 1 0 001-1z', 'M18 18c0 2-2 3-5 3'] },
    { label: 'Professionnels vérifiés', paths: ['M12 2.5l2.2 1.6 2.7-.2 1 2.5 2.3 1.5-.8 2.6.8 2.6-2.3 1.5-1 2.5-2.7-.2L12 21.5l-2.2-1.6-2.7.2-1-2.5-2.3-1.5.8-2.6-.8-2.6 2.3-1.5 1-2.5 2.7.2z', 'M12 8.5l1.1 2.3 2.5.3-1.8 1.7.5 2.5-2.3-1.2-2.3 1.2.5-2.5-1.8-1.7 2.5-.3z'] },
    { label: 'Expériences authentiques', paths: ['M5 19C4 11 9 5 20 4c0 11-6 16-14 15z', 'M5 19c3-5 6-8 10-10'] },
  ];

  destinations = [
    { name: 'Tunis & Carthage', emoji: '🏛️', count: 48, bg: 'linear-gradient(135deg,#f6a768,#d9570d)' },
    { name: 'Sidi Bou Saïd', emoji: '🏘️', count: 26, bg: 'linear-gradient(135deg,#4f8fe0,#0b2a6f)' },
    { name: 'Djerba', emoji: '🏝️', count: 35, bg: 'linear-gradient(135deg,#4fd1c5,#1e7f8c)' },
    { name: 'Sahara & Douz', emoji: '🐪', count: 22, bg: 'linear-gradient(135deg,#f2c46d,#b9741f)' },
    { name: 'Hammamet', emoji: '🌊', count: 31, bg: 'linear-gradient(135deg,#6fb7f2,#2c6fbb)' },
    { name: 'Aïn Draham', emoji: '🌲', count: 14, bg: 'linear-gradient(135deg,#6cc08b,#1f6b45)' },
  ];

  steps = [
    { title: 'Choisissez', text: 'Parcourez des centaines d’activités et de circuits proposés par des professionnels locaux.' },
    { title: 'Réservez', text: 'Réservez en quelques clics avec un paiement 100 % sécurisé et une confirmation immédiate.' },
    { title: 'Vivez l’expérience', text: 'Profitez de moments authentiques avec un guide vérifié et laissez votre avis.' },
  ];

  reviews = [
    { name: 'Sarah M.', from: 'Paris, France', text: 'Une organisation parfaite et un guide passionné à Carthage. Je recommande à 100 % !' },
    { name: 'Karim B.', from: 'Tunis, Tunisie', text: 'J’ai redécouvert mon propre pays grâce aux circuits proposés. Le Sahara était magique.' },
    { name: 'Julia W.', from: 'Berlin, Allemagne', text: 'Réservation simple, prix clairs et support très réactif. Une super expérience.' },
  ];

  email = '';
  subscribed = false;
  subscribe() { if (this.email) this.subscribed = true; }

  search() { /* TODO: brancher sur le microservice produits/offres */ }
}
