import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Subject } from 'rxjs';
import { environment } from '../../environments/environment';
import { StompStatus } from '../data/stomp-status.enum';

@Injectable({
  providedIn: 'root',
})
export class StompService {
  private client: Client | null = null;
  private messageSubject = new Subject<any>();
  public messages$ = this.messageSubject.asObservable();

  private statusSubject = new BehaviorSubject<StompStatus>(StompStatus.DISCONNECTED);
  public status$ = this.statusSubject.asObservable();

  private readonly WS_URL = environment.webSocket;

  connect(): void {
    if (this.client?.active) return;

    this.client = new Client({
      webSocketFactory: () => new SockJS(this.WS_URL),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,

      beforeConnect: () => {
        this.statusSubject.next(StompStatus.CONNECTING);
      },

      onConnect: () => {
        this.statusSubject.next(StompStatus.CONNECTED);
      },

      onDisconnect: () => {
        this.statusSubject.next(StompStatus.DISCONNECTED);
      },

      onWebSocketClose: () => {
        this.statusSubject.next(StompStatus.DISCONNECTED);
      },

      onWebSocketError: (error) => {
        console.error('WebSocket Error:', error);
      },
    });

    this.client.activate();
  }

  subscribeToFile(fileId: string) {
    if (!this.client?.active || this.statusSubject.value !== StompStatus.CONNECTED) return;
    const destination = `/topic/analysis-task/${fileId}`;
    this.client.subscribe(destination, (message: IMessage) => {
      try {
        const payload = JSON.parse(message.body);
        this.messageSubject.next(payload);
      } catch (e) {
        console.error('Errore parsing messaggio STOMP:', e);
      }
    });
  }

  startAnalysis(fileId: string): void {
    if (!this.client?.active) return;

    this.client.publish({
      destination: '/app/start-async-analyzer',
      body: fileId.trim(),
    });
  }

  disconnect(): void {
    this.client?.deactivate();
    this.client = null;
    this.statusSubject.next(StompStatus.DISCONNECTED);
  }
}
