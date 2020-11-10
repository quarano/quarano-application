import { EnrollmentCompletedGuard, ClientStoreModule } from '@qro/client/domain';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IsAuthenticatedGuard } from '@qro/auth/api';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'diary',
  },
  {
    path: 'diary',
    loadChildren: () => import('@qro/client/feature-diary').then((m) => m.ClientFeatureDiaryModule),
    canActivate: [IsAuthenticatedGuard, EnrollmentCompletedGuard],
  },
  {
    path: 'enrollment',
    loadChildren: () => import('@qro/client/feature-enrollment').then((m) => m.ClientFeatureEnrollmentModule),
  },
  {
    path: 'contact-persons',
    loadChildren: () => import('@qro/client/feature-contact-persons').then((m) => m.ClientFeatureContactPersonsModule),
    canActivate: [IsAuthenticatedGuard, EnrollmentCompletedGuard],
  },
  {
    path: 'profile',
    loadChildren: () => import('@qro/client/feature-profile').then((m) => m.ClientProfileModule),
    canActivate: [IsAuthenticatedGuard, EnrollmentCompletedGuard],
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes), ClientStoreModule],
})
export class ClientShellModule {}
