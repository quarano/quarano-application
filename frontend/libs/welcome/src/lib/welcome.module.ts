import { SharedUiTileModule } from '@qro/shared/ui-tile';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { IsAuthenticatedGuard } from '@qro/auth/api';

@NgModule({
  imports: [
    CommonModule,
    SharedUiTileModule,
    RouterModule.forChild([{ path: '', component: WelcomeComponent, canActivate: [IsAuthenticatedGuard] }]),
  ],
  declarations: [WelcomeComponent],
})
export class WelcomeModule {}
