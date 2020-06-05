import { ClientDomainModule } from '@qro/client/domain';
import { ClientProfileUiPersonalDataModule } from '@qro/client/profile/ui-personal-data';
import { ClientEnrollmentDomainModule } from '@qro/client/enrollment/domain';
import { SharedUiMultipleAutocompleteModule } from '@qro/shared/ui-multiple-autocomplete';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { SymptomsResolver, SharedUtilSymptomModule } from '@qro/shared/util-symptom';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasicDataComponent } from './components/basic-data/basic-data.component';
import { InitialQuestionaireFormComponent } from './components/initial-questionaire-form/initial-questionaire-form.component';
import { ContactPersonsResolver } from '@qro/client/contact-persons/api';
import { MyClientDataResolver } from '@qro/client/domain';
import { MyFirstQueryResolver, EncountersResolver } from '@qro/client/enrollment/domain';
import { ClientContactPersonsDomainModule } from '@qro/client/contact-persons/domain';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
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
    SharedUiMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiButtonModule,
    SharedUiMultipleAutocompleteModule,
    SharedUtilSymptomModule,
    ClientProfileUiPersonalDataModule,
    ClientContactPersonsDomainModule,
    ClientEnrollmentDomainModule,
    ClientDomainModule,
    RouterModule.forChild(routes),
  ],
})
export class ClientEnrollmentBasicDataModule {}
