import { Routes } from '@angular/router';
import { MissionsEtat } from './partage/missions-etat';

export const routesProfessionnelles: Routes = [
  {
    path: '',
    providers: [MissionsEtat],
    loadComponent: () => import('./espace-professionnel').then((m) => m.EspaceProfessionnel),
    children: [
      {
        path: 'professionnel',
        pathMatch: 'full',
        title: 'Accueil professionnel | Guidini',
        loadComponent: () => import('./accueil/accueil-professionnel').then((m) => m.AccueilProfessionnel),
      },
      {
        path: 'missions',
        title: 'Missions disponibles | Guidini',
        loadComponent: () => import('./missions/missions-disponibles').then((m) => m.MissionsDisponibles),
      },
      {
        path: 'professionnel/offres',
        title: 'Mes offres de prestation | Guidini',
        loadComponent: () => import('./offres/offres-professionnelles').then((m) => m.OffresProfessionnelles),
      },
      {
        path: 'professionnel/invitations',
        title: 'Mes invitations | Guidini',
        loadComponent: () => import('./invitations/invitations-professionnelles').then((m) => m.InvitationsProfessionnelles),
      },
    ],
  },
];
