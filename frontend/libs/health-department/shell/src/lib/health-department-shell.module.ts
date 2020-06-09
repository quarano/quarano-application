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
          import('@qro/health-department/feature-index-cases').then((m) => m.HealthDepartmentFeatureIndexCasesModule),
      },
      {
        path: 'contact-cases',
        loadChildren: () =>
          import('@qro/health-department/feature-contact-cases').then(
            (m) => m.HealthDepartmentFeatureContactCasesModule
          ),
      },
      {
        path: 'case-detail',
        loadChildren: () =>
          import('@qro/health-department/feature-case-detail').then((m) => m.HealthDepartmentFeatureCaseDetailModule),
      },
    ]),
  ],
})
export class HealthDepartmentShellModule {}
