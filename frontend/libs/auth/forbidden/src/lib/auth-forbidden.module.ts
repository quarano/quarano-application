import { SharedUtilTranslationModule } from '@qro/shared/util-translation';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ForbiddenComponent } from './forbidden/forbidden.component';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    SharedUtilTranslationModule,
    RouterModule.forChild([
      {
        path: '',
        pathMatch: 'full',
        component: ForbiddenComponent,
      },
    ]),
  ],
  declarations: [ForbiddenComponent],
})
export class AuthForbiddenModule {}
