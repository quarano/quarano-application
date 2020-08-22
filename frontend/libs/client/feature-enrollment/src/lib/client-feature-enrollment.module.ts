import { SharedUiDataProtectionModule } from '@qro/shared/ui-data-protection';
import { ClientUiPersonalDataModule } from '@qro/client/ui-personal-data';
import { SharedUiMultipleAutocompleteModule } from '@qro/shared/ui-multiple-autocomplete';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { BasicDataComponent } from './basic-data/basic-data.component';
import { SymptomsResolver, SharedUtilSymptomModule } from '@qro/shared/util-symptom';
import { MyClientDataResolver, ClientDomainModule, MyFirstQueryResolver, EncountersResolver } from '@qro/client/domain';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { ContactPersonsResolver } from '@qro/client/domain';
import { InitialQuestionaireFormComponent } from './initial-questionaire-form/initial-questionaire-form.component';
import { LandingComponent } from './landing/landing.component';
import { RegisterComponent } from './register/register.component';
import { DataProtectionDialogComponent } from './data-protection-dialog/data-protection-dialog.component';
import { ClientUiContactPersonDetailModule } from '@qro/client/ui-contact-person-detail';

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
  {
    path: 'landing',
    children: [
      { path: '', pathMatch: 'full', component: LandingComponent },
      { path: ':usertype/:clientcode', component: LandingComponent },
    ],
  },
  {
    path: 'register',
    children: [
      { path: '', component: RegisterComponent, pathMatch: 'full' },
      { path: ':clientcode', component: RegisterComponent },
    ],
  },
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
    ClientUiContactPersonDetailModule,
    ClientDomainModule,
    SharedUiDataProtectionModule,
    RouterModule.forChild(routes),
  ],
  declarations: [
    DataProtectionDialogComponent,
    BasicDataComponent,
    InitialQuestionaireFormComponent,
    LandingComponent,
    RegisterComponent,
  ],
})
export class ClientFeatureEnrollmentModule {}
