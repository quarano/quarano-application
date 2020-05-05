import { AccountEditComponent } from './account-edit/account-edit.component';
import { AccountAdministrationComponent } from './account-administration.component';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { AccountsResolver } from '@resolvers/accounts.resolver';
import { PreventUnsavedChangesGuard } from '@guards/prevent-unsaved-changes.guard';
import { AccountResolver } from '@resolvers/account.resolver';

const routes: Routes = [
  {
    path: '',
    component: AccountAdministrationComponent,
    resolve: { accounts: AccountsResolver }
  },
  {
    path: 'edit/:id',
    component: AccountEditComponent,
    resolve: { account: AccountResolver },
    canDeactivate: [PreventUnsavedChangesGuard]
  },
  {
    path: 'new',
    component: AccountEditComponent,
    resolve: { account: AccountResolver },
    canDeactivate: [PreventUnsavedChangesGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountAdministrationRoutingModule { }
