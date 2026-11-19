import { NgTemplateOutlet } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { OffresEtat } from '../offres/offres-etat';
import { IconesProfessionnelles, MissionsEtat } from '../partage/missions-etat';
import { PanneauProfessionnel } from '../partage/panneau-professionnel';

@Component({
  selector: 'app-accueil-professionnel',
  imports: [NgTemplateOutlet, RouterLink, PanneauProfessionnel],
  templateUrl: './accueil-professionnel.html',
})
export class AccueilProfessionnel {
  readonly etat = inject(MissionsEtat);
  readonly offresEtat = inject(OffresEtat);
  readonly icones = IconesProfessionnelles;
  readonly missionsDuMoment = [
    { mission: this.etat.missions[0], statut: 'En cours' },
    { mission: this.etat.missions[1], statut: 'En attente' },
  ];
  readonly rubriquesProfil = ['Informations personnelles', 'Compétences & langues', 'Disponibilités', 'Documents'];
}
