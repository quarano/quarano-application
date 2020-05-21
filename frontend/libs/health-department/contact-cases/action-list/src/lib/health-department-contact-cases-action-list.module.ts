import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActionListComponent } from './action-list/action-list.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild([{
      path: '',
      pathMatch: 'full',
      component: ActionListComponent
    }])],
  declarations: [ActionListComponent]
})
export class HealthDepartmentContactCasesActionListModule { }
