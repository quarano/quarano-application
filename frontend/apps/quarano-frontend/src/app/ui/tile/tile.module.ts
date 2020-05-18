import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TileComponent } from './tile.component';
import { AngularMaterialModule } from '../../modules/angular-material/angular-material.module';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    AngularMaterialModule
  ],
  declarations: [TileComponent],
  exports: [TileComponent]
})
export class TileModule { }
