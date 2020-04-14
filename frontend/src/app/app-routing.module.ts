import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NotFoundComponent} from './not-found/not-found.component';
import {IsAuthenticatedGuard} from './guards/is-authenticated.guard';
import {IsAuthenticatedFullyClientGuard} from './guards/is-authenticated-fully-client.guard';
import {IsHealthDepartmentUserGuard} from './guards/is-health-department-user.guard';
import {ForbiddenComponent} from './forbidden/forbidden.component';
import {PortalComponent} from './layout/portal/portal.component';
import {LandingComponent} from './landing/landing.component';
import {HomeComponent} from './landing/home/home.component';
import {TermsComponent} from './landing/terms/terms.component';
import {ImprintComponent} from './landing/imprint/imprint.component';
import {PrivacyInformationComponent} from './landing/privacy-information/privacy-information.component';

const routes: Routes = [
  {
    path: '',
    component: LandingComponent,
    children: [
      {
        path: 'home',
        component: HomeComponent
      },
      {
        path: 'terms',
        component: TermsComponent
      },
      {
        path: 'imprint',
        component: ImprintComponent
      },
      {
        path: 'privacy',
        component: PrivacyInformationComponent
      },
      {path: '', redirectTo: '/home', pathMatch: 'full'},
    ]
  },
  {
    path: '',
    component: PortalComponent,
    children: [
      {
        path: 'welcome', loadChildren: () =>
          import('./welcome/welcome.module').then(m => m.WelcomeModule)
      },
      {
        path: 'diary', loadChildren: () =>
          import('./diary/diary.module').then(m => m.DiaryModule), canActivate: [IsAuthenticatedGuard, IsAuthenticatedFullyClientGuard]
      },
      {
        path: 'contacts',
        loadChildren: () =>
          import('./contact/contact.module').then(m => m.ContactModule),
        canActivate: [IsAuthenticatedGuard, IsAuthenticatedFullyClientGuard]
      },
      {
        path: 'tenant-admin', loadChildren: () =>
          import('./tenant-admin/tenant-admin.module').then(m => m.TenantAdminModule),
        canActivate: [IsAuthenticatedGuard, IsHealthDepartmentUserGuard]
      },
      {
        path: '404/:message',
        component: NotFoundComponent,
      },
      {
        path: 'forbidden',
        component: ForbiddenComponent
      },
      {path: '', redirectTo: '/home', pathMatch: 'full'},
    ]
  },
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: '**', component: NotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
