import { SharedUtilTranslationModule } from '@qro/shared/util-translation';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HdContactComponent } from './/hd-contact/hd-contact.component';

@NgModule({
  declarations: [HdContactComponent],
  entryComponents: [HdContactComponent],
  exports: [HdContactComponent],
  imports: [CommonModule, SharedUiMaterialModule, SharedUtilTranslationModule],
})
export class ClientFeatureHealthDepartmentContactModule {}
