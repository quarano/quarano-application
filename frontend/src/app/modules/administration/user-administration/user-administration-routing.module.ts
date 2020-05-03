import { UserAdministrationComponent } from './user-administration.component';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { UserAdministrationResolver } from '@resolvers/user-administration.resolver';

const routes: Routes = [
  {
    path: '',
    component: UserAdministrationComponent,
    resolve: { users: UserAdministrationResolver }
  },
  // {
  //   path: 'edit/:id',
  //   component: DiaryEntryComponent,
  //   resolve: { diaryEntry: DiaryEntryResolver, symptoms: SymptomsResolver, contactPersons: ContactPersonsResolver },
  //   canDeactivate: [PreventUnsavedChangesGuard]
  // },
  // {
  //   path: 'new',
  //   component: DiaryEntryComponent,
  //   resolve: { diaryEntry: DiaryEntryResolver, symptoms: SymptomsResolver, contactPersons: ContactPersonsResolver },
  //   canDeactivate: [PreventUnsavedChangesGuard]
  // }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserAdministrationRoutingModule { }
