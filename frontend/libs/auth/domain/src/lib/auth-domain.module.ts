import { AuthStore } from './store/auth-store.service';
import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthInterceptor } from './interceptors/auth-interceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { StoreModule } from '@ngrx/store';
import { authFeatureKey, authReducer } from './reducers';
import { EffectsModule } from '@ngrx/effects';
import { AuthEffects } from './store/auth.effects';

@NgModule({
  imports: [CommonModule, StoreModule.forFeature(authFeatureKey, authReducer), EffectsModule.forFeature([AuthEffects])],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }, AuthStore],
})
export class AuthDomainModule {
  static forRoot(): ModuleWithProviders<AuthDomainModule> {
    return {
      ngModule: AuthDomainModule,
      providers: [AuthStore],
    };
  }
}
