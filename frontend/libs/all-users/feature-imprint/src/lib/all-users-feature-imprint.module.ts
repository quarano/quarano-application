import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImpressumComponent } from './components/impressum/impressum.component';

@NgModule({
  declarations: [ImpressumComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: '', pathMatch: 'full', component: ImpressumComponent }]),
    SharedUiMaterialModule,
  ],
})
export class AllUsersFeatureImprintModule {}
