import { SharedUiStaticPagesModule } from '@qro/shared/ui-static-pages';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImpressumComponent } from './/impressum/impressum.component';

@NgModule({
  declarations: [ImpressumComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: '', pathMatch: 'full', component: ImpressumComponent }]),
    SharedUiMaterialModule,
    SharedUiStaticPagesModule,
  ],
})
export class GeneralFeatureImprintModule {}
