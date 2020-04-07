import { ReactiveFormsModule } from '@angular/forms';
import { AngularMaterialModule } from './../angular-material/angular-material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasicDataComponent } from './basic-data.component';
import { BasicDataRoutingModule } from './basic-data-routing.module';

@NgModule({
  imports: [
    CommonModule,
    BasicDataRoutingModule,
    AngularMaterialModule,
    ReactiveFormsModule
  ],
  declarations: [BasicDataComponent]
})
export class BasicDataModule { }
