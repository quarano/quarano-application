import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgbComponent } from './components/agb/agb.component';

@NgModule({
  declarations: [AgbComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: '', pathMatch: 'full', component: AgbComponent }]),
    SharedUiMaterialModule,
  ],
})
export class AllUsersFeatureTermsModule {}
