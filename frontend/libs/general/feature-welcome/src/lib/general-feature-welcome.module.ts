import { SharedUiTileModule } from '@qro/shared/ui-tile';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IsAuthenticatedGuard } from '@qro/auth/api';
import { WelcomeComponent } from './welcome/welcome.component';

@NgModule({
  imports: [
    CommonModule,
    SharedUiTileModule,
    RouterModule.forChild([{ path: '', component: WelcomeComponent, canActivate: [IsAuthenticatedGuard] }]),
  ],
  declarations: [WelcomeComponent],
})
export class GeneralFeatureWelcomeModule {}
