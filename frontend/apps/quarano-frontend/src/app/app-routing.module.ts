import { IsAdminGuard } from '@qro/administration/api';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent, ErrorComponent } from '@qro/shared/ui-error';
import { IsAuthenticatedGuard } from '@qro/auth/api';
import { IsHealthDepartmentUserGuard } from '@qro/health-department/api';

const routes: Routes = [
  {
    path: 'client',
    loadChildren: () => import('@qro/client/shell').then((m) => m.ClientShellModule),
  },
  {
    path: 'health-department',
    loadChildren: () => import('@qro/health-department/shell').then((m) => m.HealthDepartmentShellModule),
    canActivate: [IsAuthenticatedGuard, IsHealthDepartmentUserGuard],
  },
  {
    path: 'administration',
    loadChildren: () => import('@qro/administration/shell').then((m) => m.AdministrationShellModule),
    canActivate: [IsAuthenticatedGuard, IsAdminGuard],
  },
  {
    path: 'auth',
    loadChildren: () => import('@qro/auth/shell').then((m) => m.AuthShellModule),
  },
  {
    path: 'general',
    loadChildren: () => import('@qro/general/shell').then((m) => m.GeneralShellModule),
  },
  {
    path: '404/:message',
    component: NotFoundComponent,
  },
  {
    path: 'error',
    component: ErrorComponent,
  },
  { path: '', redirectTo: 'general', pathMatch: 'full' },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload', relativeLinkResolution: 'legacy' })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
