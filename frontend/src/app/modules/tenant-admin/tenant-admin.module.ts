import {ReportCasesResolver} from '@resolvers/report-cases.resolver';
import {ActionAlertComponent} from './action-alert/action-alert.component';
import {ActionsResolver} from '@resolvers/actions.resolver';
import {ActionsComponent} from './actions/actions.component';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TenantAdminComponent} from './tenant-admin.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {TenantAdminRoutingModule} from './tenant-admin-routing.module';
import {AngularMaterialModule} from '../angular-material/angular-material.module';
import {ReactiveFormsModule} from '@angular/forms';
import {ClientsComponent} from './clients/clients.component';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {ClientComponent} from './client/client.component';
import {ReportCaseResolver} from '@resolvers/report-case.resolver';
import {EditComponent} from './client/edit/edit.component';
import {MatTabsModule} from '@angular/material/tabs';


@NgModule({
  declarations: [
    TenantAdminComponent,
    DashboardComponent,
    ClientsComponent,
    ClientComponent,
    ActionsComponent,
    ActionAlertComponent,
    EditComponent
  ],
  imports: [
    CommonModule,
    TenantAdminRoutingModule,
    AngularMaterialModule,
    ReactiveFormsModule,
    NgxDatatableModule,
    MatTabsModule
  ],
  providers: [ActionsResolver, ReportCasesResolver, ReportCaseResolver]
})
export class TenantAdminModule {
}
