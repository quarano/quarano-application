import { SharedUiButtonModule } from '@quarano-frontend/shared/ui-button';
import { AnomalyComponent } from './client/anomaly/anomaly.component';
import { ActionComponent } from './client/action/action.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TenantAdminComponent } from './tenant-admin.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TenantAdminRoutingModule } from './tenant-admin-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { ClientComponent } from './client/client.component';
import { EditComponent } from './client/edit/edit.component';
import { CommentsComponent } from './client/comments/comments.component';
import { MailComponent } from './client/mail/mail.component';
import { CloseCaseDialogComponent } from './client/close-case-dialog/close-case-dialog.component';
import { ReportCaseActionsResolver } from '../../resolvers/report-case-actions.resolver';
import { ReportCaseResolver } from '../../resolvers/report-case.resolver';
import { QuestionnaireComponent } from './client/questionnaire/questionnaire.component';
import {SharedUiMaterialModule} from '@quarano-frontend/shared/ui-material';


@NgModule({
  declarations: [
    TenantAdminComponent,
    DashboardComponent,
    ClientComponent,
    EditComponent,
    ActionComponent,
    AnomalyComponent,
    CommentsComponent,
    MailComponent,
    CloseCaseDialogComponent,
    QuestionnaireComponent
  ],
  imports: [
    CommonModule,
    TenantAdminRoutingModule,
    SharedUiMaterialModule,
    ReactiveFormsModule,
    NgxDatatableModule,
    SharedUiButtonModule
  ],
  providers: [ReportCaseResolver, ReportCaseActionsResolver]
})
export class TenantAdminModule {
}
