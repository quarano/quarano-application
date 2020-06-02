import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

const routes: Routes = [
  {
    path: 'terms',
    loadChildren: () => import('@qro/info/terms').then((m) => m.InfoTermsModule),
  },
  {
    path: 'imprint',
    loadChildren: () => import('@qro/info/imprint').then((m) => m.InfoImprintModule),
  },
  {
    path: 'data-protection',
    loadChildren: () => import('@qro/info/data-protection').then((m) => m.InfoDataProtectionModule),
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class InfoShellModule {}
