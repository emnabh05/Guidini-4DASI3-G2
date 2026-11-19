import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-signup',
  imports: [FormsModule, RouterLink],
  templateUrl: './signup.html',
  styleUrl: './signup.scss',
})
export class Signup {
  private router = inject(Router);

  showPassword = false;
  showConfirm = false;
  submitted = false;
  message = '';

  roles = [
    { id: 'touriste', label: 'Touriste', sub: '(Client)', paths: ['M12 12a4 4 0 100-8 4 4 0 000 8z', 'M4 21c0-4.5 3.6-7 8-7s8 2.5 8 7'] },
    { id: 'prestataire', label: 'Prestataire', sub: '(Agence)', paths: ['M4 9l1.5-5h13L20 9', 'M4 9a2.7 2.7 0 005.3 0 2.7 2.7 0 005.4 0A2.7 2.7 0 0020 9', 'M5 12v8h14v-8', 'M10 20v-5h4v5'] },
    { id: 'guide', label: 'Guide', sub: 'touristique', paths: ['M9 8a3 3 0 100-6 3 3 0 000 6z', 'M3 21v-6a6 6 0 0112 0v6', 'M17 3v8M17 3l4 2-4 2'] },
    { id: 'chauffeur', label: 'Chauffeur', sub: '', paths: ['M5 16l1.5-5A2 2 0 018.4 9.5h7.2a2 2 0 011.9 1.5L19 16', 'M3 16h18v3H3z', 'M6 19v2M18 19v2', 'M7 13h.01M17 13h.01'] },
  ];

  form = { role: 'touriste', firstName: '', lastName: '', email: '', dial: '+216', phone: '', password: '', confirm: '', terms: false };

  get passwordsMatch() { return this.form.password === this.form.confirm; }

  onSubmit(f: NgForm) {
    this.submitted = true;
    if (f.invalid || !this.passwordsMatch || !this.form.terms) return;
    // TODO: brancher sur le microservice d'authentification (API)
    this.message = 'Compte créé avec succès ✓';
    setTimeout(() => this.router.navigateByUrl('/'), 1200);
  }
}
