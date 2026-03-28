import { Component, DestroyRef, effect, inject, OnInit, signal } from '@angular/core';
import { RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { MatSidenav, MatSidenavContainer, MatSidenavContent } from '@angular/material/sidenav';
import { MatListItem, MatNavList } from '@angular/material/list';
import { MatIcon } from '@angular/material/icon';
import { MatIconButton, MatAnchor } from '@angular/material/button';
import { NavigationService } from './services/navigation-service';
import { StompService } from './services/web-socket-service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    MatSidenavContent,
    MatSidenav,
    MatSidenavContainer,
    MatNavList,
    MatIcon,
    MatListItem,
    MatIconButton,
    RouterLink,
    MatAnchor,
    RouterModule,
    JsonPipe,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  private nav = inject(NavigationService);
  protected readonly title = signal('vurpe-fe');
  expanded = true;

  private stompService = inject(StompService);
  private destroyRef = inject(DestroyRef); // ← qui va dichiarato!
  messages = signal<any[]>([]);
  isConnected = signal(false);

  listMenu = [
    { route: '/dashboard', title: 'Dashboard', icon: 'dashboard' },
    { route: '/files', title: 'File', icon: 'insert_drive_file' },
    { route: '/rules', title: 'Rules', icon: 'rule' },
  ];

  constructor() {
    this.stompService.messages$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((msg) => {
      console.log('📥 Messaggio ricevuto:', msg);
      this.messages.update((current) => [...current, msg]);
    });
  }

  connect() {
    this.stompService.connect();
    this.isConnected.set(true);
  }

  toggleExpand() {
    this.expanded = !this.expanded;
  }

  goBack() {
    this.nav.back();
  }

  sendTest(): void {
    const fileId = 'd2b9929b-cf63-4bc0-8aaf-cdda87d1f956';
    if (!this.isConnected()) {
      console.error('Non sei connesso! Clicca prima su Connetti.');
      return;
    }
    console.log('Avvio analisi per file:', fileId);
    this.stompService.subscribeToFile(fileId);
    setTimeout(() => {
      console.log('Avvio analisi per file:', fileId);
      this.stompService.startAnalysis(fileId);
    }, 500);
  }

  print(mess: any) {
    console.log('dio cane', mess);
  }
}
