import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderLeftComponent } from './components/header-left/header-left.component';
import { HeaderRightComponent } from './components/header-right/header-right.component';

@NgModule({
  imports: [CommonModule, RouterModule, SharedUiMaterialModule],
  declarations: [FooterComponent, HeaderLeftComponent, HeaderRightComponent],
  exports: [FooterComponent, HeaderLeftComponent, HeaderRightComponent],
})
export class LayoutModule {}
