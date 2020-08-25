import { SharedUtilTranslationModule } from '@qro/shared/util-translation';
import { metaReducers } from './reducers/index';
import { ClientStoreModule } from '@qro/client/api';
import { SharedUtilDateModule } from '@qro/shared/util-date';
import { HeaderRightComponent } from './layout/header-right/header-right.component';
import { HeaderLeftComponent } from './layout/header-left/header-left.component';
import { FooterComponent } from './layout/footer/footer.component';
import { SharedUiAsideModule } from '@qro/shared/ui-aside';
import { AuthDomainModule } from '@qro/auth/domain';
import { SharedUiErrorModule } from '@qro/shared/ui-error';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { API_URL } from '@qro/shared/util-data-access';
import { BrowserModule } from '@angular/platform-browser';
import { LOCALE_ID, NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de';
import { environment } from '../environments/environment';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { DefaultDataServiceConfig, EntityDataModule } from '@ngrx/data';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { reducers } from './reducers';
import { RouterState, StoreRouterConnectingModule } from '@ngrx/router-store';

registerLocaleData(localeDe, 'de');

function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/');
}

const SUB_MODULES = [
  SharedUiErrorModule,
  AuthDomainModule.forRoot(),
  SharedUiAsideModule,
  SharedUtilDateModule,
  SharedUiMaterialModule,
  SharedUtilTranslationModule,
];

const NGRX = [
  StoreModule.forRoot(reducers, {
    metaReducers,
    runtimeChecks: {
      strictStateImmutability: true,
      strictActionImmutability: true,
    },
  }),
  EffectsModule.forRoot([]),
  EntityDataModule.forRoot({}),
  StoreRouterConnectingModule.forRoot({
    stateKey: 'router',
    routerState: RouterState.Minimal,
  }),
  ClientStoreModule.forRoot(),
  StoreDevtoolsModule.instrument({
    maxAge: 25,
    logOnly: environment.production,
  }),
];

const NGX_TRANSLATE = [
  TranslateModule.forRoot({
    loader: {
      provide: TranslateLoader,
      useFactory: HttpLoaderFactory,
      deps: [HttpClient],
    },
    defaultLanguage: 'de',
  }),
];

const defaultDataServiceConfig: DefaultDataServiceConfig = {
  root: environment.api.baseUrl,
  timeout: 3000, // request timeout
};

@NgModule({
  declarations: [AppComponent, FooterComponent, HeaderLeftComponent, HeaderRightComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ...SUB_MODULES,
    ...NGRX,
    ...NGX_TRANSLATE,
  ],
  providers: [
    { provide: LOCALE_ID, useValue: 'de-de' },
    { provide: API_URL, useValue: environment.api.baseUrl },
    { provide: DefaultDataServiceConfig, useValue: defaultDataServiceConfig }, // @todo SC: CORE-341 - Kann die Default Config von ngrx-data gesetzt werden
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
