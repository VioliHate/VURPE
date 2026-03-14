import { Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { Dummy } from './pages/dummy/dummy';
import { FatherManager } from './pages/father-manager/father-manager';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full',
  },
  {
    path: 'dashboard',
    component: Dashboard,
    title: 'Vurpe - Dashboard',
  },
  {
    path: 'dummy',
    component: Dummy,
    title: 'Vurpe - Dummy',
  },
  {
    path: 'files',
    component: FatherManager,
    title: 'Vurpe - Files',
    data: { api: 'http://localhost:8080/call/files', serviceKey: 'files' },
  },
  {
    path: 'dataRecords',
    component: FatherManager,
    title: 'Vurpe - Data Records',
    data: { api: 'http://localhost:8080/call/data-records', serviceKey: 'files' },
  },
  {
    path: 'rules',
    component: FatherManager,
    title: 'Vurpe - rules',
    data: { api: 'http://localhost:8080/call/rules', serviceKey: 'files' },
  },
];
