import { Injectable } from '@angular/core';
import { interval, retry, Subject, tap } from 'rxjs';
import { WebSocketSubject, webSocket } from 'rxjs/webSocket';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private socket$: WebSocketSubject<any> | null = null;
  private messagesSubject$ = new Subject<any>();
  public messages$ = this.messagesSubject$.asObservable();

  private WS_URL = `ws://${environment.url}/ws/analysis`;

  connect(): void {
    if (!this.socket$ || this.socket$.closed) {
      this.socket$ = webSocket({
        url: this.WS_URL,
        openObserver: {
          next: () => {
            console.log('WebSocket connected');
          },
        },
        closeObserver: {
          next: () => {
            console.log('WebSocket disconnected');
          },
        },
      });

      this.socket$
        .pipe(
          retry({
            delay: (error, retryCount) => {
              console.log(`Retry attempt ${retryCount}`);
              return interval(3000);
            },
          }),
          tap({
            error: (error) => console.error('WebSocket error:', error),
          }),
        )
        .subscribe({
          next: (message) => this.messagesSubject$.next(message),
          error: (error) => console.error('WebSocket error:', error),
        });
    }
  }

  sendMessage(message: any): void {
    if (this.socket$) {
      this.socket$.next(message);
    }
  }

  disconnect(): void {
    if (this.socket$) {
      this.socket$.complete();
    }
  }
}
