import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [CommonModule, RouterModule.forChild([{
    path: '',
    children: [
      {
        path: 'index-cases', loadChildren: () =>
          import('@quarano-frontend/health-department/index-cases/shell')
            .then(m => m.HealthDepartmentIndexCasesShellModule),
      },
      {
        path: 'contact-cases', loadChildren: () =>
          import('@quarano-frontend/health-department/contact-cases/shell')
            .then(m => m.HealthDepartmentContactCasesShellModule),
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
export class HealthDepartmentShellModule { }
