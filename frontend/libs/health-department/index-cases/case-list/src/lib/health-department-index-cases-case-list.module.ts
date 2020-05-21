import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CaseListComponent } from './case-list/case-list.component';

@NgModule({
  imports: [CommonModule,
    RouterModule.forChild([{
      path: '',
      pathMatch: 'full',
      component: CaseListComponent
    }])],
  declarations: [CaseListComponent],
})
export class HealthDepartmentIndexCasesCaseListModule { }
