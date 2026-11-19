import { NgTemplateOutlet } from '@angular/common';
import { Component, ElementRef, effect, inject, viewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IconesProfessionnelles, MissionsEtat } from './missions-etat';

@Component({
  selector: 'app-dialogue-mission',
  imports: [FormsModule, NgTemplateOutlet],
  templateUrl: './dialogue-mission.html',
})
export class DialogueMission {
  readonly etat = inject(MissionsEtat);
  readonly icones = IconesProfessionnelles;
  private readonly dialogue = viewChild.required<ElementRef<HTMLDialogElement>>('dialogue');

  constructor() {
    effect(() => {
      const dialogue = this.dialogue().nativeElement;
      if (this.etat.action() && !dialogue.open) dialogue.showModal();
      else if (!this.etat.action() && dialogue.open) dialogue.close();
    });
  }

  fermer(): void {
    this.etat.action.set(null);
  }

  confirmer(): void {
    this.etat.confirmer();
  }
}
