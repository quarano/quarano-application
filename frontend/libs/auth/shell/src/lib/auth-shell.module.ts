import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IsAuthenticatedGuard } from '@qro/auth/domain';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: 'forbidden',
        loadChildren: () => import('@qro/auth/forbidden').then((m) => m.AuthForbiddenModule),
      },
    ]),
  ],
})
export class AuthShellModule {}
