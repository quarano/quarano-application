import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { AccountDetailResolver } from '@qro/administration/accounts/domain';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountEditComponent } from './account-edit/account-edit.component';
import { PreventUnsavedChangesGuard } from '@qro/shared/util';
import { AdministrationAccountsDomainModule } from '@qro/administration/accounts/domain';

@NgModule({
  declarations: [AccountEditComponent],
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    SharedUiButtonModule,
    FormsModule,
    ReactiveFormsModule,
    AdministrationAccountsDomainModule,
    RouterModule.forChild([
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'new'
      },
      {
        path: 'edit/:id',
        component: AccountEditComponent,
        resolve: { account: AccountDetailResolver },
        canDeactivate: [PreventUnsavedChangesGuard]
      },
      {
        path: 'new',
        component: AccountEditComponent,
        resolve: { account: AccountDetailResolver },
        canDeactivate: [PreventUnsavedChangesGuard]
      }
    ])],
})
export class AdministrationAccountsAccountDetailModule { }
