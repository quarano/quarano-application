import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ErrorInterceptorProvider } from './error.interceptor';

@NgModule({
  imports: [CommonModule],
  providers: [ErrorInterceptorProvider]
})
export class SharedUtilErrorModule { }
