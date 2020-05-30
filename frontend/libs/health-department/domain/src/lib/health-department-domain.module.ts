import { IsHealthDepartmentUserDirective } from './directives/is-health-department-user.directive';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [CommonModule],
  declarations: [IsHealthDepartmentUserDirective],
  exports: [IsHealthDepartmentUserDirective]
})
export class HealthDepartmentDomainModule { }
