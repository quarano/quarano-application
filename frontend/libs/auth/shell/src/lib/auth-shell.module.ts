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
        path: 'forbidden',
        loadChildren: () => import('@qro/auth/forbidden').then((m) => m.AuthForbiddenModule),
      },
      {
        path: 'change-password',
        loadChildren: () => import('@qro/auth/feature-change-password').then((m) => m.AuthFeatureChangePasswordModule),
        canActivate: [IsAuthenticatedGuard],
      },
      {
        path: 'login',
        loadChildren: () => import('@qro/auth/feature-login').then((m) => m.AuthFeatureLoginModule),
      },
    ]),
  ],
})
export class AuthShellModule {}
