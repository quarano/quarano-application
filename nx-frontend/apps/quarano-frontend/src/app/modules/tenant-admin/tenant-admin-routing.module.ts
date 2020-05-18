import { ActionsComponent } from './actions/actions.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TenantAdminComponent } from './tenant-admin.component';
import { ClientsComponent } from './clients/clients.component';
import { ClientComponent } from './client/client.component';
import {ReportCasesResolver} from '../../resolvers/report-cases.resolver';
import {ReportCaseResolver} from '../../resolvers/report-case.resolver';
import {ReportCaseActionsResolver} from '../../resolvers/report-case-actions.resolver';
import {ActionsResolver} from '../../resolvers/actions.resolver';

const routes: Routes = [
  {
    path: '',
    component: TenantAdminComponent,
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'clients',
        component: ClientsComponent,
        resolve: { cases: ReportCasesResolver }
      },
      {
        path: 'client/:type/:id',
        component: ClientComponent,
        resolve: { case: ReportCaseResolver, actions: ReportCaseActionsResolver }
      },
      {
        path: 'client/:type',
        component: ClientComponent,
      },
      {
        path: 'actions',
        component: ActionsComponent,
        resolve: { actions: ActionsResolver }
      },
      {
        path: '',
        redirectTo: 'clients',
        pathMatch: 'full'
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TenantAdminRoutingModule { }
