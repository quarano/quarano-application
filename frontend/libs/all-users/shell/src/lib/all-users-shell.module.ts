import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IsAuthenticatedGuard } from '@qro/auth/api';

const routes: Routes = [
  {
    path: 'terms',
    loadChildren: () => import('@qro/all-users/feature-terms').then((m) => m.AllUsersFeatureTermsModule),
  },
  {
    path: 'imprint',
    loadChildren: () => import('@qro/all-users/feature-imprint').then((m) => m.AllUsersFeatureImprintModule),
  },
  {
    path: 'data-protection',
    loadChildren: () =>
      import('@qro/all-users/feature-data-protection').then((m) => m.AllUsersFeatureDataProtectionModule),
  },
  {
    path: 'welcome',
    loadChildren: () => import('@qro/all-users/feature-welcome').then((m) => m.AllUsersFeatureWelcomeModule),
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'welcome',
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class AllUsersShellModule {}
