import { NgTemplateOutlet } from '@angular/common';
import { Component, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { OffresEtat } from '../offres/offres-etat';
import { IconesProfessionnelles, MissionsEtat } from './missions-etat';

@Component({
  selector: 'app-panneau-professionnel',
  imports: [NgTemplateOutlet, RouterLink],
  templateUrl: './panneau-professionnel.html',
  styles: ':host { display: block; min-width: 0; }',
})
export class PanneauProfessionnel {
  readonly section = input.required<'invitations' | 'offres'>();
  readonly detailOffre = input(false);
  readonly lienVisible = input(true);
  readonly etat = inject(MissionsEtat);
  readonly offresEtat = inject(OffresEtat);
  readonly icones = IconesProfessionnelles;
}
