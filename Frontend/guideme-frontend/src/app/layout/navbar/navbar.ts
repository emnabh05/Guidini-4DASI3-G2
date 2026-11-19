import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {
  liens = [
    { label: 'Activités', icone: '⛰️' },
    { label: 'Circuits', icone: '🗺️' },
    { label: 'Offres', icone: '🏷️' },
    { label: 'Communauté', icone: '👥' }
  ];
}