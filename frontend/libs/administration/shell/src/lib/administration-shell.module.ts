import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'accounts' },
  {
    path: 'accounts',
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'account-list',
      },
      {
        path: 'account-list',
        loadChildren: () =>
          import('@qro/administration/feature-account-list').then((m) => m.AdministrationFeatureAccountListModule),
      },
      {
        path: 'account-detail',
        loadChildren: () =>
          import('@qro/administration/feature-account-detail').then((m) => m.AdministrationFeatureAccountDetailModule),
      },
    ],
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class AdministrationShellModule {}
