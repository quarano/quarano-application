import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '',
        redirectTo: 'accounts',
        pathMatch: 'full',
      },
      {
        path: 'accounts',
        redirectTo: 'account-list',
        pathMatch: 'full',
        children: [
          {
            path: 'account-list',
            loadChildren: () =>
              import('@qro/administration/feature-account-list').then((m) => m.AdministrationFeatureAccountListModule),
          },
          {
            path: 'account-detail',
            loadChildren: () =>
              import('@qro/administration/feature-account-detail').then(
                (m) => m.AdministrationFeatureAccountDetailModule
              ),
          },
        ],
      },
    ]),
  ],
})
export class AdministrationShellModule {}
