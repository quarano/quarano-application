import { IsEnrolledClientDirective } from './directives/is-enrolled-client.directive';
import { AsideComponent } from './components/aside/aside.component';
import { AsideHostDirective } from './directives/aside-host.directive';
import { HasRoleDirective } from '@directives/has-role.directive';
import { ProfileModule } from './modules/profile/profile.module';
import { ErrorInterceptorProvider } from './interceptors/error.interceptor';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';
import { SnackbarService } from '@services/snackbar.service';
import { AngularMaterialModule } from './modules/angular-material/angular-material.module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { DiaryModule } from './modules/diary/diary.module';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de';
import { WelcomeModule } from './modules/welcome/welcome.module';
import { ContactModule } from './modules/contact/contact.module';
import { AuthInterceptor } from './interceptors/auth-interceptor';
import { FooterComponent } from './components/layout/footer/footer.component';
import { HeaderLeftComponent } from './components/layout/header-left/header-left.component';
import { HeaderRightComponent } from './components/layout/header-right/header-right.component';
import { ProgressBarInterceptor } from './interceptors/progress-bar.interceptor';
import { BasicDataModule } from './modules/basic-data/basic-data.module';
import { IsHealthDepartmentUserDirective } from '@directives/is-health-department-user.directive';

registerLocaleData(localeDe, 'de');

const DIRECTIVES = [
  HasRoleDirective,
  IsHealthDepartmentUserDirective,
  AsideHostDirective,
  IsEnrolledClientDirective
];

const COMPONENTS = [
  AppComponent,
  NotFoundComponent,
  FooterComponent,
  HeaderLeftComponent,
  HeaderRightComponent,
  ForbiddenComponent,
  AsideComponent
];

const SUB_MODULES = [
  DiaryModule,
  WelcomeModule,
  ContactModule,
  BasicDataModule,
  ProfileModule
];

@NgModule({
  declarations: [
    ...COMPONENTS,
    ...DIRECTIVES
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    AngularMaterialModule,
    FormsModule,
    HttpClientModule,
    ...SUB_MODULES
  ],
  providers: [
    SnackbarService,
    ErrorInterceptorProvider,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ProgressBarInterceptor,
      multi: true
    },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: LOCALE_ID, useValue: 'de-de' }
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
