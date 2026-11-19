import { Component, inject, ViewEncapsulation } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarEspace } from '../partage/navbar-espace';
import { MissionsEtat } from './partage/missions-etat';
import { DialogueMission } from './partage/dialogue-mission';

@Component({
  selector: 'app-espace-professionnel',
  imports: [NavbarEspace, RouterOutlet, DialogueMission],
  templateUrl: './espace-professionnel.html',
  styleUrls: ['./espace-professionnel.scss', './accueil/tableau-bord.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class EspaceProfessionnel {
  readonly etat = inject(MissionsEtat);
}
