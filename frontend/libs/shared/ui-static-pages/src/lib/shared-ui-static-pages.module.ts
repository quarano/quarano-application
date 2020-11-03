import { StaticPagesResolver } from './resolvers/static-pages.resolver';
import { NgModule } from '@angular/core';
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
  providers: [StaticPagesResolver, StaticPageStore],
})
export class SharedUiStaticPagesModule {}
