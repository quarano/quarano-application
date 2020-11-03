import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedUiStaticPagesModule, StaticPagesResolver } from '@qro/shared/ui-static-pages';

const routes: Routes = [
  {
    path: 'terms',
    resolve: { staticPagesLoaded: StaticPagesResolver },
    loadChildren: () => import('@qro/general/feature-terms').then((m) => m.GeneralFeatureTermsModule),
  },
  {
    path: 'imprint',
    resolve: { staticPagesLoaded: StaticPagesResolver },
    loadChildren: () => import('@qro/general/feature-imprint').then((m) => m.GeneralFeatureImprintModule),
  },
  {
    path: 'data-protection',
    resolve: { staticPagesLoaded: StaticPagesResolver },
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
  imports: [CommonModule, RouterModule.forChild(routes), SharedUiStaticPagesModule],
})
export class GeneralShellModule {}
