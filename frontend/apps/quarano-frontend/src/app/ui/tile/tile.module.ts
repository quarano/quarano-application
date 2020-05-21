import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TileComponent } from './tile.component';
import { SharedUiMaterialModule } from '@quarano-frontend/shared/ui-material';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    SharedUiMaterialModule
  ],
  declarations: [TileComponent],
  exports: [TileComponent]
})
export class TileModule { }
