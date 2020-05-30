import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IsAdminDirective } from './directives/is-admin.directive';

@NgModule({
  declarations: [IsAdminDirective],
  imports: [CommonModule],
})
export class AdministrationDomainModule { }
