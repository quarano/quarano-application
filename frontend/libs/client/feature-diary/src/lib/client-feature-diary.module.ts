import { SharedUiAlertModule } from '@qro/shared/ui-alert';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DiaryComponent } from './components/diary/diary.component';
import { DiaryResolver, DiaryDetailResolver, ClientDomainModule } from '@qro/client/domain';
import { DiaryEntryComponent } from './components/diary-entry/diary-entry.component';
import { SymptomsResolver, SharedUtilSymptomModule } from '@qro/shared/util-symptom';
import { ContactPersonsResolver } from '@qro/client/domain';
import { PreventUnsavedChangesGuard } from '@qro/shared/util-forms';
import { Routes, RouterModule } from '@angular/router';
import { ClientUiContactPersonDetailModule} from "@qro/client/ui-contact-person-detail";
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { SharedUiMultipleAutocompleteModule } from '@qro/shared/ui-multiple-autocomplete';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DiaryEntrySuccessComponent } from './components/diary-entry-success/diary-entry-success.component';
import { DiaryEntryWarningComponent } from './components/diary-entry-warning/diary-entry-warning.component';
import { DiaryListItemComponent } from './components/diary-list-item/diary-list-item.component';
import { DiaryTodayListItemComponent } from './components/diary-today-list-item/diary-today-list-item.component';
import { ForgottenContactDialogComponent } from './components/forgotten-contact-dialog/forgotten-contact-dialog.component';
import { ForgottenContactBannerComponent } from './components/forgotten-contact-banner/forgotten-contact-banner.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'diary-list',
    pathMatch: 'full',
  },
  {
    path: 'diary-list',
    component: DiaryComponent,
    resolve: { diary: DiaryResolver },
  },
  {
    path: 'diary-detail',
    children: [
      {
        path: 'edit/:id',
        component: DiaryEntryComponent,
        resolve: {
          diaryEntry: DiaryDetailResolver,
          symptoms: SymptomsResolver,
          contactPersons: ContactPersonsResolver,
        },
        canDeactivate: [PreventUnsavedChangesGuard],
      },
      {
        path: 'new/:date/:slot',
        component: DiaryEntryComponent,
        resolve: {
          diaryEntry: DiaryDetailResolver,
          symptoms: SymptomsResolver,
          contactPersons: ContactPersonsResolver,
        },
        canDeactivate: [PreventUnsavedChangesGuard],
      },
    ],
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    SharedUiMaterialModule,
    SharedUiMultipleAutocompleteModule,
    SharedUiButtonModule,
    SharedUiAlertModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUtilSymptomModule,
    ClientDomainModule,
    ClientUiContactPersonDetailModule
  ],
  declarations: [
    DiaryComponent,
    DiaryEntrySuccessComponent,
    DiaryEntryWarningComponent,
    DiaryListItemComponent,
    DiaryTodayListItemComponent,
    DiaryEntryComponent,
    ForgottenContactDialogComponent,
    ForgottenContactBannerComponent,
  ],
  entryComponents: [ForgottenContactDialogComponent],
})
export class ClientFeatureDiaryModule {}
