import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HdContactComponent } from './components/hd-contact/hd-contact.component';

@NgModule({
  declarations: [HdContactComponent],
  entryComponents: [HdContactComponent],
  exports: [HdContactComponent],
  imports: [CommonModule],
})
export class ClientFeatureHealthDepartmentContactModule {}
