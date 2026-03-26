import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { MatSidenav, MatSidenavContainer, MatSidenavContent } from '@angular/material/sidenav';
import { MatListItem, MatNavList } from '@angular/material/list';
import { MatIcon } from '@angular/material/icon';
import { MatIconButton, MatAnchor } from '@angular/material/button';
import { NavigationService } from './services/navigation-service';

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
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  private nav = inject(NavigationService);
  protected readonly title = signal('vurpe-fe');
  expanded = true;

  listMenu = [
    { route: '/dashboard', title: 'Dashboard', icon: 'dashboard' },
    { route: '/files', title: 'File', icon: 'insert_drive_file' },
    { route: '/rules', title: 'Rules', icon: 'rule' },
  ];

  toggleExpand() {
    this.expanded = !this.expanded;
  }

  goBack() {
    this.nav.back();
  }
}
