import { Component, inject, input, output, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Session } from '../../../ms1-users/session';
@Component({
  selector: 'app-admin-navigation',
  imports: [RouterLink],
  templateUrl: './admin-navigation.html',
  styleUrl: './admin-navigation.scss',
})
export class AdminNavigation {
  nav = input.required<{ label: string; icon: string }[]>();
  search = output<string>();
  action = output<string>();
  private readonly session = inject(Session);
  menuOpen = signal(false);
  logout() { this.session.logout(); }
  selected = signal('Accueil');
  navigate(label: string) {
    this.selected.set(label);
    this.menuOpen.set(false);
    if (label !== 'Accueil') this.action.emit(label);
  }
}
