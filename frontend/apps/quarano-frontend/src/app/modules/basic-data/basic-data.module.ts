import { AppFormsModule } from '../app-forms/app-forms.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasicDataComponent } from './basic-data.component';
import { BasicDataRoutingModule } from './basic-data-routing.module';
import { InitialQuestionaireFormComponent } from './initial-questionaire-form/initial-questionaire-form.component';
import { MultipleAutocompleteModule } from '../../ui/multiple-autocomplete/multiple-autocomplete.module';
import { ConfirmationDialogModule } from '../../ui/confirmation-dialog/confirmation-dialog.module';
import { MyClientDataResolver } from '../../resolvers/my-client-data.resolver';
import { EncountersResolver } from '../../resolvers/encounters.resolver';
import { MyFirstQueryResolver } from '../../resolvers/my-first-query.resolver';
import { ContactPersonsResolver } from '../../resolvers/contact-persons.resolver';
import { SharedUiMaterialModule } from '@quarano-frontend/shared/ui-material';

@NgModule({
  imports: [
    CommonModule,
    BasicDataRoutingModule,
    SharedUiMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    MultipleAutocompleteModule,
    AppFormsModule,
    ConfirmationDialogModule
  ],
  declarations: [BasicDataComponent, InitialQuestionaireFormComponent],
  providers:
    [
      ContactPersonsResolver,
      MyFirstQueryResolver,
      MyClientDataResolver,
      EncountersResolver,
    ]
})
export class BasicDataModule { }
