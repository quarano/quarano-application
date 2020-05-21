import { NgModule } from '@angular/core';
import { MatTreeModule } from '@angular/material/tree';
import {
  MatMomentDateModule, MAT_MOMENT_DATE_FORMATS,
} from '@angular/material-moment-adapter';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatExpansionModule } from '@angular/material/expansion';
import { MAT_DATE_LOCALE, MAT_DATE_FORMATS } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatListModule } from '@angular/material/list';
import { MatSortModule } from '@angular/material/sort';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSliderModule } from '@angular/material/slider';
import { MatDialogModule } from '@angular/material/dialog';
import { MatChipsModule } from '@angular/material/chips';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatStepperModule } from '@angular/material/stepper';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatMenuModule } from '@angular/material/menu';
import { MatTabsModule } from '@angular/material/tabs';

const materialModules = [
  MatButtonModule,
  MatDatepickerModule,
  MatFormFieldModule,
  MatInputModule,
  MatSelectModule,
  MatCardModule,
  MatRadioModule,
  MatTreeModule,
  MatMomentDateModule,
  MatSlideToggleModule,
  MatSnackBarModule,
  MatToolbarModule,
  MatProgressBarModule,
  MatExpansionModule,
  MatTableModule,
  MatBadgeModule,
  MatIconModule,
  MatGridListModule,
  MatListModule,
  MatSortModule,
  MatTooltipModule,
  MatSliderModule,
  MatDialogModule,
  MatChipsModule,
  MatAutocompleteModule,
  MatStepperModule,
  MatCheckboxModule,
  MatMenuModule,
  MatTabsModule
];

@NgModule({
  imports: [...materialModules],
  exports: [...materialModules],
  declarations: [],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'de' },
    { provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS }
  ]
})
export class SharedUiMaterialModule { }
