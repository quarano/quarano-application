import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '',
        redirectTo: 'index-cases',
        pathMatch: 'full',
      },
      {
        path: 'index-cases',
        loadChildren: () =>
          import('@qro/health-department/index-cases/shell').then((m) => m.HealthDepartmentIndexCasesShellModule),
      },
      {
        path: 'contact-cases',
        loadChildren: () =>
          import('@qro/health-department/contact-cases/shell').then((m) => m.HealthDepartmentContactCasesShellModule),
      },
      {
        path: 'case-detail',
        loadChildren: () =>
          import('@qro/health-department/case-detail').then((m) => m.HealthDepartmentCaseDetailModule),
      },
    ]),
  ],
})
export class HealthDepartmentShellModule {}
