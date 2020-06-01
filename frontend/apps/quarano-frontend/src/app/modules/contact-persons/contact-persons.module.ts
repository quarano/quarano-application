import { AppFormsModule } from '../app-forms/app-forms.module';
import { RouterModule } from '@angular/router';
import { ContactPersonComponent } from './contact-person/contact-person.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactRoutingModule } from './contact-persons-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { ContactPersonsComponent } from './contact-persons.component';
import { SharedUiAlertModule } from '@qro/shared/ui-alert';
import { SharedUiConfirmationDialogModule } from '@qro/shared/ui-confirmation-dialog';
import { ContactPersonsResolver } from '@qro/client/contact-persons/domain';
import { ContactPersonResolver } from '../../resolvers/contact-person.resolver';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';

const COMPONENTS = [ContactPersonsComponent, ContactPersonComponent];

@NgModule({
  imports: [
    CommonModule,
    ContactRoutingModule,
    SharedUiMaterialModule,
    ReactiveFormsModule,
    RouterModule,
    SharedUiAlertModule,
    SharedUiConfirmationDialogModule,
    AppFormsModule,
  ],
  declarations: [...COMPONENTS],
  providers: [ContactPersonsResolver, ContactPersonResolver],
})
export class ContactPersonsModule {}
