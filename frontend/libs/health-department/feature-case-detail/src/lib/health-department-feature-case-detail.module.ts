import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { SymptomsResolver, SharedUtilSymptomModule } from '@qro/shared/util-symptom';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { CaseDetailComponent } from './components/case-detail/case-detail.component';
import {
  ReportCaseResolver,
  ReportCaseActionsResolver,
  HealthDepartmentDomainModule,
} from '@qro/health-department/domain';
import { QuestionnaireComponent } from './components/questionnaire/questionnaire.component';
import { MailComponent } from './components/mail/mail.component';
import { IndexContactsComponent } from './components/index-contacts/index-contacts.component';
import { EditComponent } from './components/edit/edit.component';
import { CommentsComponent } from './components/comments/comments.component';
import { CloseCaseDialogComponent } from './components/close-case-dialog/close-case-dialog.component';
import { AnomalyComponent } from './components/anomaly/anomaly.component';
import { ActionComponent } from './components/action/action.component';

const routes: Routes = [
  {
    path: ':type/:id',
    component: CaseDetailComponent,
    resolve: { case: ReportCaseResolver, actions: ReportCaseActionsResolver, symptoms: SymptomsResolver },
  },
  {
    path: ':type',
    component: CaseDetailComponent,
  },
];

@NgModule({
  declarations: [
    ActionComponent,
    AnomalyComponent,
    CaseDetailComponent,
    CloseCaseDialogComponent,
    CommentsComponent,
    EditComponent,
    IndexContactsComponent,
    MailComponent,
    QuestionnaireComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    SharedUiMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    SharedUiButtonModule,
    HealthDepartmentDomainModule,
    SharedUtilSymptomModule,
  ],
})
export class HealthDepartmentFeatureCaseDetailModule {}
