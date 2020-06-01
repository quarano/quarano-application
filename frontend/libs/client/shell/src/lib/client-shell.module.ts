import { EnrollmentCompletedGuard } from '@qro/client/enrollment/api';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IsAuthenticatedGuard } from '@qro/auth/api';

const routes: Routes = [
  {
    path: 'diary',
    loadChildren: () => import('@qro/client/diary/shell').then((m) => m.ClientDiaryShellModule),
    canActivate: [IsAuthenticatedGuard, EnrollmentCompletedGuard],
  },
  {
    path: 'enrollment',
    loadChildren: () => import('@qro/client/enrollment/shell').then((m) => m.ClientEnrollmentShellModule),
  },
  {
    path: 'contact-persons',
    loadChildren: () => import('@qro/client/contact-persons/shell').then((m) => m.ClientContactPersonsShellModule),
    canActivate: [IsAuthenticatedGuard, EnrollmentCompletedGuard],
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class ClientShellModule {}
