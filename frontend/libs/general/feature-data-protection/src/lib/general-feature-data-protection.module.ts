import { SharedUiStaticPagesModule } from '@qro/shared/ui-static-pages';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataProtectionCardComponent } from './data-protection-card/data-protection-card.component';

@NgModule({
  declarations: [DataProtectionCardComponent],
  exports: [DataProtectionCardComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: '', pathMatch: 'full', component: DataProtectionCardComponent }]),
    SharedUiMaterialModule,
    SharedUiStaticPagesModule,
  ],
})
export class GeneralFeatureDataProtectionModule {}
