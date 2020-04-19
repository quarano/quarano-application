import { BasicDataGuard } from './guards/basic-data.guard';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { IsAuthenticatedGuard } from '@guards/is-authenticated.guard';
import { IsAuthenticatedFullyClientGuard } from '@guards/is-authenticated-fully-client.guard';
import { IsHealthDepartmentUserGuard } from '@guards/is-health-department-user.guard';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';

const routes: Routes = [
  {
    path: 'welcome', loadChildren: () =>
      import('./modules/welcome/welcome.module').then(m => m.WelcomeModule)
  },
  {
    path: 'diary', loadChildren: () =>
      import('./modules/diary/diary.module').then(m => m.DiaryModule),
    canActivate: [IsAuthenticatedGuard, IsAuthenticatedFullyClientGuard]
  },
  {
    path: 'contacts', loadChildren: () =>
      import('./modules/contact/contact.module').then(m => m.ContactModule),
    canActivate: [IsAuthenticatedGuard, IsAuthenticatedFullyClientGuard]
  },
  {
    path: 'profile', loadChildren: () =>
      import('./modules/profile/profile.module').then(m => m.ProfileModule),
    canActivate: [IsAuthenticatedGuard, IsAuthenticatedFullyClientGuard]
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
