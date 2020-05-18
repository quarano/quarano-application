import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import {IsAdminGuard} from '../../guards/is-admin.guard';
import {IsAuthenticatedGuard} from '../../guards/is-authenticated.guard';

const routes: Routes = [
  {
    path: '',
    canActivate: [IsAuthenticatedGuard, IsAdminGuard],
    canActivateChild: [IsAuthenticatedGuard, IsAdminGuard],
    redirectTo: 'accounts',
    pathMatch: 'full'
  },
  {
    path: 'accounts', loadChildren: () =>
      import('./account-administration/account-administration.module').then(m => m.AccountAdministrationModule)
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdministrationRoutingModule { }
