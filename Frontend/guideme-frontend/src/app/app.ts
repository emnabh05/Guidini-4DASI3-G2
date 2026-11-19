import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Navbar } from './core/navbar/navbar';
import { Footer } from './core/footer/footer';

@Component({
  imports: [RouterOutlet, Navbar, Footer],
  selector: 'app-root',
  template: `@if (!hideChrome) { <app-navbar /> }<router-outlet />@if (!hideChrome) { <app-footer /> }`,
})
export class App {
  private readonly router = inject(Router);

  /** Pages plein écran sans navbar/footer publics (inscription, espace admin). */
  get hideChrome() {
    const path = this.router.url.split(/[?#]/)[0];
    return ['/register', '/signup', '/missions', '/professionnel'].includes(path) || path.startsWith('/professionnel/') || path.startsWith('/admin') || path.startsWith('/touriste') || path.startsWith('/prestataire');
  }
}
