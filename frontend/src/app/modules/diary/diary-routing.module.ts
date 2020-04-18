import { ContactPersonsResolver } from '@resolvers/contact-persons.resolver';
import { GroupedDiaryEntriesResolver } from '@resolvers/grouped-diary-entries.resolver';
import { SymptomsResolver } from '@resolvers/symptoms.resolver';
import { DiaryEntryResolver } from '@resolvers/diary-entry.resolver';
import { DiaryComponent } from './diary.component';
import { DiaryEntryComponent } from './diary-entry/diary-entry.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PreventUnsavedChangesGuard } from '@guards/prevent-unsaved-changes.guard';


const routes: Routes = [
  {
    path: '',
    component: DiaryComponent,
    resolve: { diaryEntries: GroupedDiaryEntriesResolver }
  },
  {
    path: 'edit/:id',
    component: DiaryEntryComponent,
    resolve: { diaryEntry: DiaryEntryResolver, symptoms: SymptomsResolver, contactPersons: ContactPersonsResolver },
    canDeactivate: [PreventUnsavedChangesGuard]
  },
  {
    path: 'new',
    component: DiaryEntryComponent,
    resolve: { diaryEntry: DiaryEntryResolver, symptoms: SymptomsResolver, contactPersons: ContactPersonsResolver },
    canDeactivate: [PreventUnsavedChangesGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DiaryRoutingModule { }
