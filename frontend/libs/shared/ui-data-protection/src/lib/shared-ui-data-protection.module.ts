import { DataProtectionComponent } from './components/data-protection/data-protection.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [CommonModule],
  declarations: [DataProtectionComponent],
  exports: [DataProtectionComponent],
})
export class SharedUiDataProtectionModule {}
