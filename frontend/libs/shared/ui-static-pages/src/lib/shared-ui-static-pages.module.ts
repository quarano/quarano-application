import { StaticPageComponent } from './static-page/static-page.component';
import { StaticPagesResolver } from './resolvers/static-pages.resolver';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
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
  declarations: [StaticPageComponent],
  exports: [StaticPageComponent],
})
export class SharedUiStaticPagesModule {}
