import { Component } from '@angular/core';

@Component({
  selector: 'app-accueil',
  templateUrl: './accueil.html',
  styleUrl: './accueil.css'
})
export class Accueil {
  categories = [
    { label: 'Culture', icone: '🏛️' },
    { label: 'Nature', icone: '🍃' },
    { label: 'Aventure', icone: '🧗' },
    { label: 'Mer & Plage', icone: '🏖️' },
    { label: 'Gastronomie', icone: '🍴' }
  ];

  experiences = [
    { titre: 'Visite de Carthage', categorie: 'Culture', duree: '1/2 journée', prix: 45, note: 4.8, avis: 320, image: '/images/carthage.jpg' },
    { titre: 'Découverte de Sidi Bou Saïd', categorie: 'Culture', duree: '1 journée', prix: 60, note: 4.7, avis: 280, image: '/images/sidi-bou-said.jpg' },
    { titre: 'Aventure dans le Sahara', categorie: 'Aventure', duree: '3 jours', prix: 350, note: 4.9, avis: 410, image: '/images/sahara.jpg' },
    { titre: 'Randonnée à Aïn Draham', categorie: 'Nature', duree: '1 journée', prix: 70, note: 4.6, avis: 190, image: '/images/ain-draham.jpg' }
  ];

  garanties = [
    { icone: '🛡️', label: 'Réservation sécurisée' },
    { icone: '🎧', label: 'Support 24/7' },
    { icone: '✅', label: 'Professionnels vérifiés' },
    { icone: '🌿', label: 'Expériences authentiques' }
  ];
}