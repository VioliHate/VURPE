import {Component, inject, signal} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import {
  MatDrawer,
  MatDrawerContainer,
  MatSidenav,
  MatSidenavContainer,
  MatSidenavContent
} from '@angular/material/sidenav';
import {MatIcon} from '@angular/material/icon';
import {MatButton, MatIconButton, MatMiniFabButton} from '@angular/material/button';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {map, Observable, shareReplay} from 'rxjs';
import {MatToolbar} from '@angular/material/toolbar';
import {AsyncPipe} from '@angular/common';
import {MatListItem, MatListItemIcon, MatNavList} from '@angular/material/list';
@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    MatSlideToggleModule,
    MatSidenav,

    MatSidenavContent,

    MatIcon,

    MatSidenavContainer,
    MatToolbar,
    AsyncPipe,
    MatNavList,
    MatListItemIcon,
    MatListItem,
    MatIconButton
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {

  isExpanded = false;

breakpoint=inject(BreakpointObserver)

  isHandset$: Observable<boolean> = this.breakpoint
    .observe(Breakpoints.Handset)
    .pipe(
      map((result) => result.matches),
      shareReplay()
    );



  toggleSideNav() {
    this.isExpanded = !this.isExpanded;
  }

}
