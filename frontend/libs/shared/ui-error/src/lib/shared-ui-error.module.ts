import { RouterModule } from '@angular/router';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotFoundComponent } from './/not-found/not-found.component';
import { ErrorInterceptorProvider } from './interceptors/error.interceptor';

@NgModule({
  imports: [CommonModule, SharedUiMaterialModule, RouterModule],
  declarations: [NotFoundComponent],
  exports: [NotFoundComponent],
  providers: [ErrorInterceptorProvider],
})
export class SharedUiErrorModule {}
