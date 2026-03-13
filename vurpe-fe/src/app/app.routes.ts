import { Routes } from '@angular/router';
import {Dashboard} from './pages/dashboard/dashboard';
import {Dummy} from './pages/dummy/dummy';
import {Files} from './pages/files/files';

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
  },
  {
    path: 'files',
    component:Files,
    title:'Vurpe - Files',
    data: { api: 'http://localhost:8080/call/files'}
  },
   {
    path: 'rules',
    component:Files,
    title:'Vurpe - rules',
    data: { api: 'http://localhost:8080/call/rules'}
  }

];
