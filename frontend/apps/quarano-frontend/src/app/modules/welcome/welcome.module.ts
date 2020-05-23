import { SharedUiButtonModule } from '@quarano-frontend/shared/ui-button';
import { SharedUiTileModule } from '@quarano-frontend/shared/ui-tile';
import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { WelcomeComponent } from './welcome.component';
import { WelcomeRoutingModule } from './welcome-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { LandingComponent } from './landing/landing.component';
import { SharedUiMaterialModule } from '@quarano-frontend/shared/ui-material';

export const DATE_FORMAT = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'LL',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};


@NgModule({
  declarations: [
    WelcomeComponent,
    RegisterComponent,
    LoginComponent,
    LandingComponent
  ],
  imports: [
    CommonModule,
    WelcomeRoutingModule,
    SharedUiMaterialModule,
    FormsModule,
    ClipboardModule,
    ReactiveFormsModule,
    SharedUiTileModule,
    SharedUiButtonModule
  ],
  providers: [
    DatePipe
  ]
})
export class WelcomeModule { }
