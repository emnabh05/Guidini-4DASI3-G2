import { Routes } from '@angular/router';
import { Accueil } from './ms2-produit-offreTouristique/accueil/accueil';
import { SeConnecter } from './ms1-users/se-connecter/se-connecter';
import { Signup } from './ms1-users/signup/signup';
import { Touriste } from './espaces/touriste/touriste';
import { Prestataire } from './espaces/prestataire/prestataire';
import { SectionPage } from './ms2-produit-offreTouristique/sections/section-page';
import { AdminDashboard } from './espaces/admin/admin-dashboard/admin-dashboard';
import { AdminAccueil } from './espaces/admin/admin-accueil/admin-accueil';
import { Communaute } from './ms6-posts/communaute/communaute';

export const routes: Routes = [
  { path: '', component: Accueil },
  { path: 'login', component: SeConnecter },
  { path: 'register', component: Signup },
  { path: 'signup', component: Signup },
  { path: 'touriste', component: Touriste },
  { path: 'prestataire', component: Prestataire },
  { path: 'admin', component: AdminDashboard },
  { path: 'admin-legacy', component: AdminAccueil },
  { path: 'communaute', component: Communaute },
  ...['the-camp', 'trips', 'accommodation', 'experiences', 'activities'].map((path) => ({ path, component: SectionPage, data: { section: path } })),
  { path: '**', redirectTo: '' },
];
