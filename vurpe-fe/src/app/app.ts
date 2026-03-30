import { StompStatus } from './data/stomp-status.enum';
import { Component, DestroyRef, inject, signal } from '@angular/core';
import { RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { MatSidenav, MatSidenavContainer, MatSidenavContent } from '@angular/material/sidenav';
import { MatListItem, MatNavList } from '@angular/material/list';
import { MatIcon } from '@angular/material/icon';
import { MatIconButton } from '@angular/material/button';
import { NavigationService } from './services/navigation-service';
import { StompService } from './services/web-socket-service';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { MatTooltip } from '@angular/material/tooltip';

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
    RouterModule,
    MatTooltip,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly StompStatus = StompStatus;

  private nav = inject(NavigationService);
  protected readonly title = signal('vurpe-fe');
  expanded = true;

  private stompService = inject(StompService);
  private destroyRef = inject(DestroyRef);
  messages = signal<any[]>([]);
  status = toSignal(this.stompService.status$, { initialValue: StompStatus.DISCONNECTED });

  listMenu = [
    { route: '/dashboard', title: 'Dashboard', icon: 'dashboard' },
    { route: '/files', title: 'File', icon: 'insert_drive_file' },
    { route: '/rules', title: 'Rules', icon: 'rule' },
  ];

  constructor() {
    this.connect();

    this.stompService.messages$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((msg) => {
      this.messages.update((current) => [...current, msg]);
    });
  }

  connect() {
    this.stompService.connect();
  }

  toggleExpand() {
    this.expanded = !this.expanded;
  }

  goBack() {
    this.nav.back();
  }

  sendTest(): void {
    const fileId = 'd2b9929b-cf63-4bc0-8aaf-cdda87d1f956';
    if (this.status() !== StompStatus.CONNECTED) {
      return;
    }
    this.stompService.subscribeToFile(fileId);
    setTimeout(() => {
      this.stompService.startAnalysis(fileId);
    }, 500);
  }
}
