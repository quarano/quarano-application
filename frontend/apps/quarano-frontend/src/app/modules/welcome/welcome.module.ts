import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiTileModule } from '@qro/shared/ui-tile';
import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { WelcomeComponent } from './welcome.component';
import { WelcomeRoutingModule } from './welcome-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { RegisterComponent } from './register/register.component';
import { LandingComponent } from './landing/landing.component';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';

@NgModule({
  declarations: [
    WelcomeComponent,
    RegisterComponent,
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
