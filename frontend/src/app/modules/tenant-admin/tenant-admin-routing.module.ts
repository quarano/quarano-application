import { ReportCasesResolver } from '@resolvers/report-cases.resolver';
import { ActionsResolver } from '@resolvers/actions.resolver';
import { ActionsComponent } from './actions/actions.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TenantAdminComponent } from './tenant-admin.component';
import { ClientsComponent } from './clients/clients.component';

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
