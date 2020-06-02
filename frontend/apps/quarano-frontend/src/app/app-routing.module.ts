import { IsAdminGuard } from '@qro/administration/api';
import { DataProtectionComponent } from './components/data-protection/data-protection.component';
import { ImpressumComponent } from './components/impressum/impressum.component';
import { AgbComponent } from './components/agb/agb.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { IsAuthenticatedGuard } from '@qro/auth/api';
import { IsHealthDepartmentUserGuard } from '@qro/health-department/api';

const routes: Routes = [
  {
    path: 'welcome',
    loadChildren: () => import('./modules/welcome/welcome.module').then((m) => m.WelcomeModule),
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
    path: 'agb',
    component: AgbComponent,
  },
  {
    path: 'impressum',
    component: ImpressumComponent,
  },
  {
    path: 'data-protection',
    component: DataProtectionComponent,
  },
  {
    path: '404/:message',
    component: NotFoundComponent,
  },
  { path: '', redirectTo: 'auth', pathMatch: 'full' },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
