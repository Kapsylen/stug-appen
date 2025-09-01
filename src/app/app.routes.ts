import { Routes } from '@angular/router';
import {NotFoundComponent} from './components/not-found/not-found.component';
import {ForbiddenComponent} from './components/forbidden/forbidden.component';
import {HomeComponent} from './components/home/home.component';
import {UtlaggComponent} from './components/utlagglista/utlagg.component';
import {FakturaComponents} from './components/fakturor/faktura/faktura.components';
import {canActivateAuthRole} from './guards/auth-role.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'utlagg',
    component: UtlaggComponent,
    canActivate: [canActivateAuthRole],
    data: { role: 'view-utlagg' }
  },
  {
    path: 'fakturor',
    component: FakturaComponents,
    canActivate: [canActivateAuthRole],
    data: { role: 'view-fakturor' }
  },
  {
    path: 'kontakter',
    component: FakturaComponents,
    canActivate: [canActivateAuthRole],
    data: { role: 'view-fakturor' }
  },
  {
    path: 'arenden',
    component: FakturaComponents,
    canActivate: [canActivateAuthRole],
    data: { role: 'view-fakturor' }
  },
  { path: 'forbidden', component: ForbiddenComponent },
  { path: '**', component: NotFoundComponent }
];
