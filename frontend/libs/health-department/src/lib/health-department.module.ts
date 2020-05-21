import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [CommonModule, RouterModule.forChild([{
    path: '',
    children: [
      {
        path: 'index-cases', loadChildren: () =>
          import('@quarano-frontend/health-department/index-cases')
            .then(m => m.HealthDepartmentIndexCasesModule),
      },
      {
        path: 'contact-cases', loadChildren: () =>
          import('@quarano-frontend/health-department/contact-cases')
            .then(m => m.HealthDepartmentContactCasesModule),
      },
      {
        path: '',
        redirectTo: 'index-cases',
        pathMatch: 'full'
      }
    ]
  },

  ])],
})
export class HealthDepartmentModule { }
