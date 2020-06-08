import { ClientUiPersonalDataModule } from '@qro/client/ui-personal-data';
import { SharedUiMultipleAutocompleteModule } from '@qro/shared/ui-multiple-autocomplete';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { BasicDataComponent } from './components/basic-data/basic-data.component';
import { SymptomsResolver, SharedUtilSymptomModule } from '@qro/shared/util-symptom';
import { MyClientDataResolver, ClientDomainModule, MyFirstQueryResolver, EncountersResolver } from '@qro/client/domain';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { ContactPersonsResolver } from '@qro/client/domain';
import { InitialQuestionaireFormComponent } from './components/initial-questionaire-form/initial-questionaire-form.component';
import { LandingComponent } from './components/landing/landing.component';
import { RegisterComponent } from './components/register/register.component';

const routes: Routes = [
  {
    path: 'basic-data',
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
  { path: 'landing:usertype/:clientcode', component: LandingComponent },
  { path: 'landing', component: LandingComponent, pathMatch: 'full' },
  { path: 'register:clientcode', component: RegisterComponent },
  { path: 'register', component: RegisterComponent, pathMatch: 'full' },
];

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiButtonModule,
    SharedUiMultipleAutocompleteModule,
    SharedUtilSymptomModule,
    ClientUiPersonalDataModule,
    ClientDomainModule,
    RouterModule.forChild(routes),
  ],
  declarations: [BasicDataComponent, InitialQuestionaireFormComponent, LandingComponent, RegisterComponent],
})
export class ClientFeatureEnrollmentModule {}
