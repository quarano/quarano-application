import { StaticPagesResolver } from './resolvers/static-pages.resolver';
import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StaticPageStore } from './store/static-page-store.service';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { staticPageFeatureKey, staticPageReducer } from './reducers';
import { StaticPageEffects } from './store/static-page.effects';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(staticPageFeatureKey, staticPageReducer),
    EffectsModule.forFeature([StaticPageEffects]),
  ],
  providers: [StaticPagesResolver],
})
export class SharedUiStaticPagesModule {
  static forRoot(): ModuleWithProviders<SharedUiStaticPagesModule> {
    return {
      ngModule: SharedUiStaticPagesModule,
      providers: [StaticPageStore, StaticPagesResolver],
    };
  }
}
