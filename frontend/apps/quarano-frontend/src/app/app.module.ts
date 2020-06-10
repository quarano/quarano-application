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
import { HttpClientModule } from '@angular/common/http';
import { registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de';
import { environment } from '../environments/environment';

registerLocaleData(localeDe, 'de');

const SUB_MODULES = [
  SharedUiErrorModule,
  AuthDomainModule,
  SharedUiAsideModule,
  SharedUtilDateModule,
  SharedUiMaterialModule,
];

@NgModule({
  declarations: [AppComponent, FooterComponent, HeaderLeftComponent, HeaderRightComponent],
  imports: [BrowserModule, BrowserAnimationsModule, AppRoutingModule, FormsModule, HttpClientModule, ...SUB_MODULES],
  providers: [
    { provide: LOCALE_ID, useValue: 'de-de' },
    { provide: API_URL, useValue: environment.api.baseUrl },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
