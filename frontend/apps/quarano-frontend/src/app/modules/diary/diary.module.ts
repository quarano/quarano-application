import { DiaryTodayListItemComponent } from './diary-today-list-item/diary-today-list-item.component';
import { DiaryEntrySuccessComponent } from './diary-entry-success/diary-entry-success.component';
import { DiaryEntryWarningComponent } from './diary-entry-warning/diary-entry-warning.component';
import { ForgottenContactDialogComponent } from './forgotten-contact-dialog/forgotten-contact-dialog.component';
import { DiaryListItemComponent } from './diary-list-item/diary-list-item.component';
import { RouterModule } from '@angular/router';
import { DiaryEntryComponent } from './diary-entry/diary-entry.component';
import { DiaryRoutingModule } from './diary-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DiaryComponent } from './diary.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ForgottenContactBannerComponent } from './forgotten-contact-banner/forgotten-contact-banner.component';
import { AlertModule } from '../../ui/alert/alert.module';
import { MultipleAutocompleteModule } from '../../ui/multiple-autocomplete/multiple-autocomplete.module';
import { ConfirmationDialogModule } from '../../ui/confirmation-dialog/confirmation-dialog.module';
import { DiaryEntryResolver } from '../../resolvers/diary-entry.resolver';
import { SymptomsResolver } from '../../resolvers/symptoms.resolver';
import { DiaryResolver } from '../../resolvers/diary.resolver';
import { SharedUiMaterialModule } from '@quarano-frontend/shared/ui-material';

const COMPONENTS = [
  DiaryComponent,
  DiaryEntryComponent,
  DiaryListItemComponent,
  ForgottenContactBannerComponent,
  ForgottenContactDialogComponent,
  DiaryEntryWarningComponent,
  DiaryEntrySuccessComponent,
  DiaryTodayListItemComponent
];

@NgModule({
  imports: [
    CommonModule,
    DiaryRoutingModule,
    SharedUiMaterialModule,
    ReactiveFormsModule,
    RouterModule,
    AlertModule,
    ConfirmationDialogModule,
    MultipleAutocompleteModule
  ],
  declarations: [...COMPONENTS],
  entryComponents: [ForgottenContactBannerComponent, ForgottenContactDialogComponent],
  providers:
    [
      DiaryEntryResolver,
      SymptomsResolver,
      DiaryResolver
    ]
})
export class DiaryModule { }
