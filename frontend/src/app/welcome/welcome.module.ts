import { NgModule } from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import { WelcomeComponent } from './welcome.component';
import { WelcomeRoutingModule } from './welcome-routing.module';
import { AngularMaterialModule } from '../angular-material/angular-material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { CreateUserComponent } from './create-user/create-user.component';
import { MatStepperModule } from '@angular/material/stepper';
import { DetailsComponent } from './create-user/details/details.component';
import { FirstQueryComponent } from './create-user/first-query/first-query.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import {MAT_DATE_FORMATS} from '@angular/material/core';

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
    CreateUserComponent,
    DetailsComponent,
    FirstQueryComponent,
    RegisterComponent,
    LoginComponent
  ],
  imports: [
    CommonModule,
    WelcomeRoutingModule,
    AngularMaterialModule,
    FormsModule,
    ClipboardModule,
    MatStepperModule,
    ReactiveFormsModule,
  ],
  providers: [
    {provide: MAT_DATE_FORMATS, useValue: DATE_FORMAT},
    DatePipe
    ]
})
export class WelcomeModule { }
