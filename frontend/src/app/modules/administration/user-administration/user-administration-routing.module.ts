import { UserEditComponent } from './user-edit/user-edit.component';
import { UserAdministrationComponent } from './user-administration.component';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { UserAdministrationResolver } from '@resolvers/user-administration.resolver';
import { PreventUnsavedChangesGuard } from '@guards/prevent-unsaved-changes.guard';
import { UserResolver } from '@resolvers/user.resolver';

const routes: Routes = [
  {
    path: '',
    component: UserAdministrationComponent,
    resolve: { users: UserAdministrationResolver }
  },
  {
    path: 'edit/:id',
    component: UserEditComponent,
    resolve: { user: UserResolver },
    canDeactivate: [PreventUnsavedChangesGuard]
  },
  {
    path: 'new',
    component: UserEditComponent,
    resolve: { user: UserResolver },
    canDeactivate: [PreventUnsavedChangesGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserAdministrationRoutingModule { }
