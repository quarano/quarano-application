import { IsAdminGuard } from './guards/is-admin.guard';
import { DataProtectionComponent } from './components/data-protection/data-protection.component';
import { ImpressumComponent } from './components/impressum/impressum.component';
import { AgbComponent } from './components/agb/agb.component';
import { BasicDataGuard } from './guards/basic-data.guard';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';
import {EnrollmentCompletedGuard} from './guards/enrollment-completed.guard';
import {IsAuthenticatedGuard} from './guards/is-authenticated.guard';
import {IsHealthDepartmentUserGuard} from './guards/is-health-department-user.guard';

const routes: Routes = [
  {
    path: 'welcome', loadChildren: () =>
      import('./modules/welcome/welcome.module').then(m => m.WelcomeModule)
  },
  {
    path: 'diary', loadChildren: () =>
      import('./modules/diary/diary.module').then(m => m.DiaryModule),
    canActivate: [IsAuthenticatedGuard, EnrollmentCompletedGuard]
  },
  {
    path: 'contact-persons', loadChildren: () =>
      import('./modules/contact-persons/contact-persons.module').then(m => m.ContactPersonsModule),
    canActivate: [IsAuthenticatedGuard, EnrollmentCompletedGuard]
  },
  {
    path: 'profile', loadChildren: () =>
      import('./modules/profile/profile.module').then(m => m.ProfileModule),
    canActivate: [IsAuthenticatedGuard, EnrollmentCompletedGuard]
  },
  {
    path: 'tenant-admin', loadChildren: () =>
      import('./modules/tenant-admin/tenant-admin.module').then(m => m.TenantAdminModule),
    canActivate: [IsAuthenticatedGuard, IsHealthDepartmentUserGuard]
  },
  {
    path: 'basic-data', loadChildren: () =>
      import('./modules/basic-data/basic-data.module').then(m => m.BasicDataModule),
    canActivate: [IsAuthenticatedGuard, BasicDataGuard]
  },
  {
    path: 'administration', loadChildren: () =>
      import('./modules/administration/administration.module').then(m => m.AdministrationModule),
    canActivate: [IsAuthenticatedGuard, IsAdminGuard]
  },

  {
    path: 'agb', component: AgbComponent
  },
  {
    path: 'impressum', component: ImpressumComponent
  },
  {
    path: 'data-protection', component: DataProtectionComponent
  },
  {
    path: '404/:message',
    component: NotFoundComponent,
  },
  {
    path: 'forbidden',
    component: ForbiddenComponent
  },
  { path: '', redirectTo: 'welcome', pathMatch: 'full' },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
