import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserResolver } from '@resolvers/user.resolver';
import { UserEditComponent } from './user-edit/user-edit.component';
import { ConfirmationDialogModule } from '@ui/confirmation-dialog/confirmation-dialog.module';
import { AngularMaterialModule } from './../../angular-material/angular-material.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserAdministrationComponent } from './user-administration.component';
import { UserAdministrationRoutingModule } from './user-administration-routing.module';
import { UserAdministrationResolver } from '@resolvers/user-administration.resolver';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';

@NgModule({
  imports: [
    CommonModule,
    UserAdministrationRoutingModule,
    AngularMaterialModule,
    NgxDatatableModule,
    ConfirmationDialogModule,
    FormsModule,
    ReactiveFormsModule
  ],
  declarations: [UserAdministrationComponent, UserEditComponent],
  providers: [UserAdministrationResolver, UserResolver]
})
export class UserAdministrationModule { }
