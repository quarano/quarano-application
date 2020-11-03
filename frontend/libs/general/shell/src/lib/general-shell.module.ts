import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

const routes: Routes = [
  {
    path: 'terms',
    loadChildren: () => import('@qro/general/feature-terms').then((m) => m.GeneralFeatureTermsModule),
  },
  {
    path: 'imprint',
    loadChildren: () => import('@qro/general/feature-imprint').then((m) => m.GeneralFeatureImprintModule),
  },
  {
    path: 'data-protection',
    loadChildren: () =>
      import('@qro/general/feature-data-protection').then((m) => m.GeneralFeatureDataProtectionModule),
  },
  {
    path: 'welcome',
    loadChildren: () => import('@qro/general/feature-welcome').then((m) => m.GeneralFeatureWelcomeModule),
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
export class GeneralShellModule {}
