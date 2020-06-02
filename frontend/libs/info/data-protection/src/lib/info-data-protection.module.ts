import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataProtectionComponent } from './components/data-protection/data-protection.component';

@NgModule({
  declarations: [DataProtectionComponent],
  exports: [DataProtectionComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: '', pathMatch: 'full', component: DataProtectionComponent }]),
    SharedUiMaterialModule,
  ],
})
export class InfoDataProtectionModule {}
