import { Component, input } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { ExecutionTerminal } from '../execution-terminal/execution-terminal';

@Component({
  selector: 'app-analizer',
  imports: [MatButton, MatIcon, ExecutionTerminal],
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
