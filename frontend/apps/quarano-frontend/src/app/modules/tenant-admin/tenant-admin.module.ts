import { AnomalyComponent } from './client/anomaly/anomaly.component';
import { ActionComponent } from './client/action/action.component';
import { ActionAlertComponent } from './action-alert/action-alert.component';
import { ActionsComponent } from './actions/actions.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TenantAdminComponent } from './tenant-admin.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TenantAdminRoutingModule } from './tenant-admin-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { ClientsComponent } from './clients/clients.component';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { ClientComponent } from './client/client.component';
import { EditComponent } from './client/edit/edit.component';
import { CommentsComponent } from './client/comments/comments.component';
import { MailComponent } from './client/mail/mail.component';
import { CloseCaseDialogComponent } from './client/close-case-dialog/close-case-dialog.component';
import { ReportCasesResolver } from '../../resolvers/report-cases.resolver';
import { ReportCaseActionsResolver } from '../../resolvers/report-case-actions.resolver';
import { ReportCaseResolver } from '../../resolvers/report-case.resolver';
import { ActionsResolver } from '../../resolvers/actions.resolver';
import { SharedUiMaterialModule } from '@quarano-frontend/shared/ui-material';


@NgModule({
  declarations: [
    TenantAdminComponent,
    DashboardComponent,
    ClientsComponent,
    ClientComponent,
    ActionsComponent,
    ActionAlertComponent,
    EditComponent,
    ActionComponent,
    AnomalyComponent,
    CommentsComponent,
    MailComponent,
    CloseCaseDialogComponent
  ],
  imports: [
    CommonModule,
    TenantAdminRoutingModule,
    SharedUiMaterialModule,
    ReactiveFormsModule,
    NgxDatatableModule
  ],
  providers: [ActionsResolver, ReportCasesResolver, ReportCaseResolver, ReportCaseActionsResolver]
})
export class TenantAdminModule {
}
