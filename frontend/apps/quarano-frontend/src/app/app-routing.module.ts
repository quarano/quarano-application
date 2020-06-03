import { IsAdminGuard } from '@qro/administration/api';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from '@qro/shared/util-error';
import { IsAuthenticatedGuard } from '@qro/auth/api';
import { IsHealthDepartmentUserGuard } from '@qro/health-department/api';

const routes: Routes = [
  {
    path: 'welcome',
    loadChildren: () => import('@qro/welcome').then((m) => m.WelcomeModule),
  },
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
    path: 'info',
    loadChildren: () => import('@qro/info/shell').then((m) => m.InfoShellModule),
  },
  {
    path: '404/:message',
    component: NotFoundComponent,
  },
  { path: '', redirectTo: 'welcome', pathMatch: 'full' },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
