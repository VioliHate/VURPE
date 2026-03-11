import { Routes } from '@angular/router';
import {Dashboard} from './pages/dashboard/dashboard';
import {Dummy} from './pages/dummy/dummy';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    component:Dashboard,
    title:'Vurpe - Dashboard'
  },
  {
    path: 'dummy',
    component:Dummy,
    title:'Vurpe - Dummy'
  }

];
