import { SharedUiAsideModule } from '@qro/shared/ui-aside';
import { AuthDomainModule } from '@qro/auth/domain';
import { SharedUtilErrorModule } from '@qro/shared/util-error';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { API_URL } from '@qro/shared/util';
import { TenantAdminModule } from './modules/tenant-admin/tenant-admin.module';
import { DataProtectionComponent } from './components/data-protection/data-protection.component';
import { ImpressumComponent } from './components/impressum/impressum.component';
import { AgbComponent } from './components/agb/agb.component';
import { ProfileModule } from './modules/profile/profile.module';
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
import { ContactPersonsModule } from './modules/contact-persons/contact-persons.module';
import { FooterComponent } from './components/layout/footer/footer.component';
import { HeaderLeftComponent } from './components/layout/header-left/header-left.component';
import { HeaderRightComponent } from './components/layout/header-right/header-right.component';
import { BasicDataModule } from './modules/basic-data/basic-data.module';
import { DateInterceptor } from './interceptors/date-interceptor';
import { HdContactComponent } from './components/hd-contact/hd-contact.component';
import { environment } from '../environments/environment';

registerLocaleData(localeDe, 'de');

const COMPONENTS = [
  AppComponent,
  NotFoundComponent,
  FooterComponent,
  HeaderLeftComponent,
  HeaderRightComponent,
  AgbComponent,
  ImpressumComponent,
  DataProtectionComponent,
  HdContactComponent,
];

const SUB_MODULES = [
  WelcomeModule,
  ContactPersonsModule,
  BasicDataModule,
  ProfileModule,
  TenantAdminModule,
  SharedUtilErrorModule,
  AuthDomainModule,
  SharedUiAsideModule,
];

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
  entryComponents: [HdContactComponent],
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
