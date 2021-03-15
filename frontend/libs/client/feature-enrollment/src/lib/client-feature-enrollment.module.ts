import { ClientUiLocationDetailModule } from '@qro/client/ui-location-detail';
import { IsAuthenticatedGuard } from '@qro/auth/api';
import { ClientUiPersonalDataModule } from '@qro/client/ui-personal-data';
import { SharedUiMultipleAutocompleteModule } from '@qro/shared/ui-multiple-autocomplete';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { BasicDataComponent } from './basic-data/basic-data.component';
import { SymptomsResolver, SharedUtilSymptomModule } from '@qro/shared/util-symptom';
import {
  ClientDomainModule,
  MyFirstQueryResolver,
  EncountersResolver,
  EnrollmentProfileResolver,
  LocationsResolver,
} from '@qro/client/domain';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { ContactPersonsResolver } from '@qro/client/domain';
import { InitialQuestionnaireFormComponent } from './initial-questionnaire-form/initial-questionnaire-form.component';
import { LandingComponent } from './landing/landing.component';
import { RegisterComponent } from './register/register.component';
import { DataProtectionDialogComponent } from './data-protection-dialog/data-protection-dialog.component';
import { ClientUiContactPersonDetailModule } from '@qro/client/ui-contact-person-detail';
import { SharedUtilTranslationModule } from '@qro/shared/util-translation';
import { HealthDepartmentAddressComponent } from './health-department-address/health-department-address.component';
import { SharedUiStaticPagesModule, StaticPagesResolver } from '@qro/shared/ui-static-pages';

const routes: Routes = [
  {
    path: 'basic-data',
    pathMatch: 'full',
    component: BasicDataComponent,
    resolve: {
      symptomsLoaded: SymptomsResolver,
      contactPersons: ContactPersonsResolver,
      locations: LocationsResolver,
      firstQuery: MyFirstQueryResolver,
      clientData: EnrollmentProfileResolver,
      encounters: EncountersResolver,
    },
    canActivate: [IsAuthenticatedGuard],
  },
  {
    path: 'landing',
    resolve: { staticPagesLoaded: StaticPagesResolver },
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
  {
    path: 'health-department',
    pathMatch: 'full',
    component: HealthDepartmentAddressComponent,
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
    ClientUiLocationDetailModule,
    ClientDomainModule,
    RouterModule.forChild(routes),
    SharedUtilTranslationModule,
    SharedUiStaticPagesModule,
  ],
  declarations: [
    DataProtectionDialogComponent,
    BasicDataComponent,
    InitialQuestionnaireFormComponent,
    LandingComponent,
    RegisterComponent,
    HealthDepartmentAddressComponent,
  ],
})
export class ClientFeatureEnrollmentModule {}
