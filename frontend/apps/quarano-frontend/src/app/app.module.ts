import { SharedUiAsideModule } from '@qro/shared/ui-aside';
import { AuthDomainModule } from '@qro/auth/domain';
import { SharedUtilErrorModule } from '@qro/shared/util-error';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { API_URL } from '@qro/shared/util';
import { BrowserModule } from '@angular/platform-browser';
import { LOCALE_ID, NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de';
import { WelcomeModule } from './modules/welcome/welcome.module';
import { FooterComponent } from './components/layout/footer/footer.component';
import { HeaderLeftComponent } from './components/layout/header-left/header-left.component';
import { HeaderRightComponent } from './components/layout/header-right/header-right.component';
import { DateInterceptor } from './interceptors/date-interceptor';
import { environment } from '../environments/environment';

registerLocaleData(localeDe, 'de');

const COMPONENTS = [AppComponent, NotFoundComponent, FooterComponent, HeaderLeftComponent, HeaderRightComponent];

const SUB_MODULES = [WelcomeModule, SharedUtilErrorModule, AuthDomainModule, SharedUiAsideModule];

@NgModule({
  declarations: [...COMPONENTS],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    SharedUiMaterialModule,
    FormsModule,
    HttpClientModule,
    ...SUB_MODULES,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: DateInterceptor,
      multi: true,
    },
    { provide: LOCALE_ID, useValue: 'de-de' },
    { provide: API_URL, useValue: environment.api.baseUrl },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
