import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PanneauProfessionnel } from '../partage/panneau-professionnel';

@Component({
  selector: 'app-invitations-professionnelles',
  imports: [RouterLink, PanneauProfessionnel],
  template: `
    <section class="banniere">
  <h1>Mes invitations</h1>
</section>
    <div class="vue-seule">
      <a routerLink="/missions" class="lien-action retour">Retour aux missions</a>
      <app-panneau-professionnel section="invitations" [lienVisible]="false" />
    </div>
  `,
})
export class InvitationsProfessionnelles {}
