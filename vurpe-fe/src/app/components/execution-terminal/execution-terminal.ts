import { Component, DestroyRef, inject, signal } from '@angular/core';
import { StompService } from '../../services/web-socket-service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-execution-terminal',
  imports: [],
  templateUrl: './execution-terminal.html',
  styleUrl: './execution-terminal.scss',
})
export class ExecutionTerminal {
  private stompService = inject(StompService);
  private destroyRef = inject(DestroyRef);
  messages = signal<any[]>([]);

  constructor() {
    this.stompService.messages$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((msg) => {
      this.messages.update((current) => [...current, msg]);
    });
  }
}
