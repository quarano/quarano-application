import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from './dashboard/dashboard.component';
import {TenantAdminComponent} from './tenant-admin.component';
import {ClientComponent} from './client/client.component';
import {ReportCaseResolver} from '../../resolvers/report-case.resolver';
import {ReportCaseActionsResolver} from '../../resolvers/report-case-actions.resolver';
import {SymptomsResolver} from '../../resolvers/symptoms.resolver';

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
        path: 'client/:type/:id',
        component: ClientComponent,
        resolve: {case: ReportCaseResolver, actions: ReportCaseActionsResolver, symptoms: SymptomsResolver}
      },
      {
        path: 'client/:type',
        component: ClientComponent,
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
export class TenantAdminRoutingModule {
}
