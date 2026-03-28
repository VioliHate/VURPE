import { Component, input } from '@angular/core';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { CamelToTitlePipe } from '../../pipe/CamelToTitlePipe';
import { StatusBadgeDirective } from '../../directives/status-badge-directive';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-stats-card',
  imports: [
    MatCardModule,
    MatChipsModule,
    MatProgressBarModule,
    CamelToTitlePipe,
    StatusBadgeDirective,
    MatIcon,
  ],
  templateUrl: './stats-card.html',
  styleUrl: './stats-card.scss',
})
export class StatsCard {
  list = input.required<any>();
  title = input.required<string>();

  constructor() {}
}
