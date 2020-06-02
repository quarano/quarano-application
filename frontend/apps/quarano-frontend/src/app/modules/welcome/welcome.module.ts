import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiTileModule } from '@qro/shared/ui-tile';
import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { WelcomeComponent } from './welcome.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';

@NgModule({
  declarations: [WelcomeComponent],
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    FormsModule,
    ClipboardModule,
    ReactiveFormsModule,
    SharedUiTileModule,
    SharedUiButtonModule,
  ],
  providers: [DatePipe],
})
export class WelcomeModule {}
