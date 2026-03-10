import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {MatListItem, MatNavList} from '@angular/material/list';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MatSidenavContent, MatSidenav, MatSidenavContainer, MatNavList, MatIcon, MatListItem, MatIconButton],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('vurpe-fe');
  expanded = true;   // parte aperto in versione estesa

  toggleExpand() {
    this.expanded = !this.expanded;
  }

}
