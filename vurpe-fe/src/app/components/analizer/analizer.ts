import { Component, input } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-analizer',
  imports: [MatButton, MatIcon],
  templateUrl: './analizer.html',
  styleUrl: './analizer.scss',
})
export class Analizer {
  fileId = input<string | undefined>();
  fileStatus = input<string>();
  isStarted: boolean = false;

  startAnalize() {
    this.isStarted = !this.isStarted;
  }
}
