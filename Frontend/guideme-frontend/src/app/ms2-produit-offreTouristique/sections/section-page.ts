import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { map } from 'rxjs';
import { SECTIONS } from './sections.data';

@Component({
  selector: 'app-section-page',
  imports: [RouterLink],
  templateUrl: './section-page.html',
  styleUrl: './section-page.scss',
})
export class SectionPage {
  private readonly route = inject(ActivatedRoute);
  private readonly key = toSignal(this.route.data.pipe(map((d) => d['section'] as string)), { initialValue: null });

  section = computed(() => SECTIONS.find((s) => s.key === this.key()) ?? SECTIONS[0]);
  all = SECTIONS;
}
