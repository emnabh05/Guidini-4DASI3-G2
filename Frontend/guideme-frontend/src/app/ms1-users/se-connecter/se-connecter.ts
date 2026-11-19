import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-se-connecter',
  imports: [FormsModule, RouterLink],
  templateUrl: './se-connecter.html',
  styleUrl: './se-connecter.scss',
})
export class SeConnecter {
  private router = inject(Router);

  showPassword = false;
  submitted = false;
  message = '';

  login = { email: '', password: '', remember: false };

  onSubmit(form: NgForm) {
    this.submitted = true;
    if (form.invalid) return;
    // TODO: brancher sur le microservice d'authentification (API)
    this.message = 'Connexion réussie ✓';
    setTimeout(() => this.router.navigateByUrl('/'), 1200);
  }

  social(provider: 'google' | 'facebook') {
    // TODO: OAuth
    this.message = `Connexion avec ${provider} bientôt disponible`;
  }
}
