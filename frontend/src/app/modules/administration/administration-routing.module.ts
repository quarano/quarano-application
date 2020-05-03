import { IsAdminGuard } from '@guards/is-admin.guard';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { IsAuthenticatedGuard } from '@guards/is-authenticated.guard';

const routes: Routes = [
  {
    path: '',
    canActivate: [IsAuthenticatedGuard, IsAdminGuard],
    canActivateChild: [IsAuthenticatedGuard, IsAdminGuard],
    redirectTo: 'users'
  },
  {
    path: 'users', loadChildren: () =>
      import('./user-administration/user-administration.module').then(m => m.UserAdministrationModule)
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdministrationRoutingModule { }
