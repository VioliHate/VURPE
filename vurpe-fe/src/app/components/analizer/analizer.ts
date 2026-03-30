import { Component, inject, input } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { ExecutionTerminal } from '../execution-terminal/execution-terminal';
import { StompService } from '../../services/web-socket-service';

@Component({
  selector: 'app-analizer',
  imports: [MatIcon, ExecutionTerminal],
  templateUrl: './analizer.html',
  styleUrl: './analizer.scss',
})
export class Analizer {
  fileId = input.required<string>();
  fileStatus = input<string>();
  isStarted: boolean = false;

  private stompService = inject(StompService);

  startAnalize() {
    this.isStarted = !this.isStarted;

    this.stompService.subscribeToFile(this.fileId());
    setTimeout(() => {
      this.stompService.startAnalysis(this.fileId());
    }, 500);
  }
}
