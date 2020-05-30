import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IsAuthenticatedGuard } from '@qro/auth/domain';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'login',
      },
      {
        path: 'change-password', loadChildren: () =>
          import('@qro/auth/change-password').then(m => m.AuthChangePasswordModule),
        canActivate: [IsAuthenticatedGuard]
      },
      {
        path: 'login', loadChildren: () =>
          import('@qro/auth/login').then(m => m.AuthLoginModule)
      },
      {
        path: 'forbidden', loadChildren: () =>
          import('@qro/auth/forbidden').then(m => m.AuthForbiddenModule)
      }])]
})
export class AuthShellModule { }
