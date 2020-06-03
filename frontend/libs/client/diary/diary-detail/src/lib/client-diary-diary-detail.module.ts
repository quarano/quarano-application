import { ClientDiaryDomainModule, DiaryDetailResolver } from '@qro/client/diary/domain';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiMultipleAutocompleteModule } from '@qro/shared/ui-multiple-autocomplete';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { SymptomsResolver, SharedUtilSymptomModule } from '@qro/shared/util-symptom';
import { ContactPersonsResolver } from '@qro/client/contact-persons/api';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DiaryEntryComponent } from './components/diary-entry/diary-entry.component';
import { PreventUnsavedChangesGuard } from '@qro/shared/util';

const routes: Routes = [
  {
    path: 'edit/:id',
    component: DiaryEntryComponent,
    resolve: { diaryEntry: DiaryDetailResolver, symptoms: SymptomsResolver, contactPersons: ContactPersonsResolver },
    canDeactivate: [PreventUnsavedChangesGuard],
  },
  {
    path: 'new/:date/:slot',
    component: DiaryEntryComponent,
    resolve: { diaryEntry: DiaryDetailResolver, symptoms: SymptomsResolver, contactPersons: ContactPersonsResolver },
    canDeactivate: [PreventUnsavedChangesGuard],
  },
];

@NgModule({
  declarations: [DiaryEntryComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    SharedUiMaterialModule,
    SharedUiMultipleAutocompleteModule,
    SharedUiButtonModule,
    FormsModule,
    ReactiveFormsModule,
    ClientDiaryDomainModule,
    SharedUtilSymptomModule,
  ],
})
export class ClientDiaryDiaryDetailModule {}
