import { SnackbarService } from './services/snackbar.service';
import { AngularMaterialModule } from './angular-material/angular-material.module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { DiaryModule } from './diary/diary.module';
import { NotFoundComponent } from './not-found/not-found.component';
import { registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de';
import { WelcomeModule } from './welcome/welcome.module';
import { ContactModule } from './contact/contact.module';
import { AuthInterceptor } from './interceptors/auth-interceptor';
import { FooterComponent } from './layout/footer/footer.component';
import { HeaderLeftComponent } from './layout/header-left/header-left.component';
import { HeaderRightComponent } from './layout/header-right/header-right.component';

registerLocaleData(localeDe, 'de');

@NgModule({
  declarations: [
    AppComponent,
    NotFoundComponent,
    FooterComponent,
    HeaderLeftComponent,
    HeaderRightComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    AngularMaterialModule,
    FormsModule,
    HttpClientModule,
    DiaryModule,
    WelcomeModule,
    ContactModule
  ],
  providers: [
    SnackbarService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
