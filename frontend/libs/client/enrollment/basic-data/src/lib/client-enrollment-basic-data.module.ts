import { ClientProfileModule } from '@qro/client/profile';
import { SharedUiMultipleAutocompleteModule } from '@qro/shared/ui-multiple-autocomplete';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { SymptomsResolver } from '@qro/shared/util-symptom';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasicDataComponent } from './components/basic-data/basic-data.component';
import { InitialQuestionaireFormComponent } from './components/initial-questionaire-form/initial-questionaire-form.component';
import { ContactPersonsResolver } from '@qro/client/contact-persons/api';
import { MyClientDataResolver } from '@qro/client/domain';
import { MyFirstQueryResolver, EncountersResolver } from '@qro/client/enrollment/domain';

const routes: Routes = [
  {
    path: '',
    component: BasicDataComponent,
    resolve: {
      symptoms: SymptomsResolver,
      contactPersons: ContactPersonsResolver,
      firstQuery: MyFirstQueryResolver,
      clientData: MyClientDataResolver,
      encounters: EncountersResolver,
    },
  },
];

@NgModule({
  declarations: [BasicDataComponent, InitialQuestionaireFormComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    SharedUiMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiButtonModule,
    SharedUiMultipleAutocompleteModule,
    ClientProfileModule,
  ],
})
export class ClientEnrollmentBasicDataModule {}
