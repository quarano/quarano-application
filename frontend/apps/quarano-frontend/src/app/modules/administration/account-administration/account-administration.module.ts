import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AccountEditComponent } from './account-edit/account-edit.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountAdministrationComponent } from './account-administration.component';
import { AccountAdministrationRoutingModule } from './account-administration-routing.module';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { ConfirmationDialogModule } from '../../../ui/confirmation-dialog/confirmation-dialog.module';
import { AccountsResolver } from '../../../resolvers/accounts.resolver';
import { AccountResolver } from '../../../resolvers/account.resolver';
import { SharedUiMaterialModule } from '@quarano-frontend/shared/ui-material';

@NgModule({
  imports: [
    CommonModule,
    AccountAdministrationRoutingModule,
    SharedUiMaterialModule,
    NgxDatatableModule,
    ConfirmationDialogModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  declarations: [AccountAdministrationComponent, AccountEditComponent],
  providers: [AccountsResolver, AccountResolver]
})
export class AccountAdministrationModule { }
