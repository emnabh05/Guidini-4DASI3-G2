import { NgTemplateOutlet } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IconesProfessionnelles, MissionsEtat } from '../partage/missions-etat';
import { PanneauProfessionnel } from '../partage/panneau-professionnel';

@Component({
  selector: 'app-missions-disponibles',
  imports: [NgTemplateOutlet, FormsModule, PanneauProfessionnel],
  templateUrl: './missions-disponibles.html',
})
export class MissionsDisponibles {
  readonly etat = inject(MissionsEtat);
  readonly icones = IconesProfessionnelles;
  readonly lieux = this.etat.missions.map(({ lieu }) => lieu);
  readonly resultats = this.etat.resultats;
  recherche = this.etat.filtres().recherche;
  lieu = this.etat.filtres().lieu;
  periode = this.etat.filtres().periode;
  type = this.etat.filtres().type;

  filtrer(): void {
    this.etat.filtres.set({ recherche: this.recherche, lieu: this.lieu, periode: this.periode, type: this.type });
  }

  reinitialiser(): void {
    this.recherche = this.lieu = this.periode = this.type = '';
    this.filtrer();
  }
}
