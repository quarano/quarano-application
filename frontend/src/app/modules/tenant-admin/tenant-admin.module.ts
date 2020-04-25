import { ActionAlertComponent } from './action-alert/action-alert.component';
import { ActionsResolver } from './../../resolvers/actions.resolver';
import { ActionsComponent } from './actions/actions.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TenantAdminComponent } from './tenant-admin.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TenantAdminRoutingModule } from './tenant-admin-routing.module';
import { AngularMaterialModule } from '../angular-material/angular-material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { ClientsComponent } from './clients/clients.component';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';


@NgModule({
  declarations: [TenantAdminComponent, DashboardComponent, ClientsComponent, ActionsComponent, ActionAlertComponent],
  imports: [
    CommonModule,
    TenantAdminRoutingModule,
    AngularMaterialModule,
    ReactiveFormsModule,
    NgxDatatableModule
  ],
  providers: [ActionsResolver]
})
export class TenantAdminModule { }
