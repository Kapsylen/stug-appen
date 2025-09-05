import { Routes } from '@angular/router';
import {NotFoundComponent} from './components/not-found/not-found.component';
import {ForbiddenComponent} from './components/forbidden/forbidden.component';
import {HomeComponent} from './components/home/home.component';
import {UtlaggComponent} from './components/utlagglista/utlagg.component';
import {FakturaComponents} from './components/fakturor/faktura/faktura.components';
import {canActivateAuthRole} from './guards/auth-role.guard';
import {KontakterComponent} from './components/kontakter/kontakter.component';
import {ArendenComponent} from './components/arenden/arenden.component';

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
    component: KontakterComponent,
    canActivate: [canActivateAuthRole],
    data: { role: 'view-kontakter' }
  },
  {
    path: 'arenden',
    component: ArendenComponent,
    canActivate: [canActivateAuthRole],
    data: { role: 'view-arenden' }
  },
  { path: 'forbidden', component: ForbiddenComponent },
  { path: '**', component: NotFoundComponent }
];
