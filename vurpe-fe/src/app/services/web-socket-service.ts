import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Subject } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class StompService {
  private client: Client | null = null;
  private messageSubject = new Subject<any>();
  public messages$ = this.messageSubject.asObservable();
  private connectedSubject = new BehaviorSubject<boolean>(false);
  public connected$ = this.connectedSubject.asObservable();

  private readonly WS_URL = environment.webSocket;

  connect(): void {
    if (this.client?.active) {
      console.log('STOMP già connesso');
      return;
    }

    this.client = new Client({
      webSocketFactory: () => new SockJS(this.WS_URL),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,

      onConnect: () => {
        console.log('✅ STOMP Connected');
        this.connectedSubject.next(true); // Avvisa che siamo pronti
      },

      onStompError: (frame) => {
        console.error('❌ STOMP Error:', frame);
      },

      onWebSocketError: (error) => {
        console.error('WebSocket Error:', error);
      },
    });

    this.client.activate();
  }

  subscribeToFile(fileId: string): void {
    if (!this.client?.active) {
      console.warn('STOMP non connesso, non posso sottoscrivere');
      return;
    }

    const destination = `/topic/analysis-task/${fileId}`;

    this.client.subscribe(destination, (message: IMessage) => {
      try {
        const payload = JSON.parse(message.body);
        console.log(`📥 Ricevuto su ${destination}:`, payload);
        this.messageSubject.next(payload);
      } catch (e) {
        console.error('Errore parsing messaggio STOMP:', e, message.body);
      }
    });

    console.log(`📌 Sottoscritto al topic: ${destination}`);
  }

  startAnalysis(fileId: string): void {
    if (!this.client?.active) {
      console.warn('STOMP non connesso');
      return;
    }

    this.client.publish({
      destination: '/app/start-async-analyzer',
      body: fileId.trim(),
    });

    console.log(`📤 Inviato comando start-analysis per file: ${fileId}`);
  }

  disconnect(): void {
    this.client?.deactivate();
    this.client = null;
  }
}
