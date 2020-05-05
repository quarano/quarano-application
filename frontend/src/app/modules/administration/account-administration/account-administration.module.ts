import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AccountResolver } from '@resolvers/account.resolver';
import { AccountEditComponent } from './account-edit/account-edit.component';
import { ConfirmationDialogModule } from '@ui/confirmation-dialog/confirmation-dialog.module';
import { AngularMaterialModule } from '../../angular-material/angular-material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountAdministrationComponent } from './account-administration.component';
import { AccountAdministrationRoutingModule } from './account-administration-routing.module';
import { AccountsResolver } from '@resolvers/accounts.resolver';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';

@NgModule({
  imports: [
    CommonModule,
    AccountAdministrationRoutingModule,
    AngularMaterialModule,
    NgxDatatableModule,
    ConfirmationDialogModule,
    FormsModule,
    ReactiveFormsModule
  ],
  declarations: [AccountAdministrationComponent, AccountEditComponent],
  providers: [AccountsResolver, AccountResolver]
})
export class AccountAdministrationModule { }
