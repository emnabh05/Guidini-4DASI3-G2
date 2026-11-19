import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-footer',
  imports: [RouterLink],
  templateUrl: './footer.html',
  styleUrl: './footer.scss',
})
export class Footer {
  year = new Date().getFullYear();
  cols = [
    { title: 'Explorer', links: ['Activités', 'Circuits', 'Offres', 'Hébergements', 'Communauté'] },
    { title: 'Guidini', links: ['À propos', 'Nos professionnels', 'Blog', 'Carrières', 'Presse'] },
    { title: 'Aide', links: ['Centre d’aide', 'Contact', 'Conditions de réservation', 'Politique d’annulation', 'FAQ'] },
  ];
  socials = ['Facebook', 'Instagram', 'YouTube', 'TikTok'];
}
