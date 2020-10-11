import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { languageFeatureKey, languageReducer } from './reducers';
import { LanguageEffects } from './store/language.effects';
import { LanguageInterceptor } from './services/language.interceptor';

function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, 'assets/i18n/');
}

@NgModule({
  imports: [
    CommonModule,
    TranslateModule.forChild({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
      defaultLanguage: 'de',
    }),
    StoreModule.forFeature(languageFeatureKey, languageReducer),
    EffectsModule.forFeature([LanguageEffects]),
  ],
  exports: [TranslateModule],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: LanguageInterceptor, multi: true }],
})
export class SharedUtilTranslationModule {}
