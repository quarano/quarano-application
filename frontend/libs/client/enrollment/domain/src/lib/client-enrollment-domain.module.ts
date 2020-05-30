import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IsEnrolledClientDirective } from './directives/is-enrolled-client.directive';

@NgModule({
  declarations: [IsEnrolledClientDirective],
  imports: [CommonModule],
  exports: [IsEnrolledClientDirective]
})
export class ClientEnrollmentDomainModule { }
