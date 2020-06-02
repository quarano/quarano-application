import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasicDataGuard } from '@qro/client/enrollment/domain';
import { IsAuthenticatedGuard } from '@qro/auth/api';

const routes: Routes = [
  {
    path: 'basic-data',
    loadChildren: () => import('@qro/client/enrollment/basic-data').then((m) => m.ClientEnrollmentBasicDataModule),
    canActivate: [IsAuthenticatedGuard, BasicDataGuard],
  },
  {
    path: 'landing',
    loadChildren: () => import('@qro/client/enrollment/landing').then((m) => m.ClientEnrollmentLandingModule),
  },
  {
    path: 'register',
    loadChildren: () => import('@qro/client/enrollment/register').then((m) => m.ClientEnrollmentRegisterModule),
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class ClientEnrollmentShellModule {}
