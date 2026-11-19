import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';

/** Gestion de session : à brancher plus tard sur le microservice d'authentification. */
@Injectable({ providedIn: 'root' })
export class Session {
  private readonly router = inject(Router);

  logout() {
    try {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      sessionStorage.clear();
    } catch { /* stockage indisponible */ }
    this.router.navigateByUrl('/login');
  }
}
