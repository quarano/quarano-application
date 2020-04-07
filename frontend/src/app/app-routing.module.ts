import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './not-found/not-found.component';
import { IsAuthenticatedGuard } from './guards/is-authenticated.guard';
import {IsAuthenticatedFullyGuard} from './guards/is-authenticated-fully.guard';
import { ForbiddenComponent } from './forbidden/forbidden.component';

const routes: Routes = [
  {
    path: 'welcome', loadChildren: () =>
      import('./welcome/welcome.module').then(m => m.WelcomeModule)
  },
  {
    path: 'diary', loadChildren: () =>
      import('./diary/diary.module').then(m => m.DiaryModule), canActivate: [IsAuthenticatedGuard, IsAuthenticatedFullyGuard]
  },
  {
    path: 'contacts', loadChildren: () =>
      import('./contact/contact.module').then(m => m.ContactModule), canActivate: [IsAuthenticatedGuard, IsAuthenticatedFullyGuard]
  },
  {
    path: 'tenant-admin', loadChildren: () =>
      import('./tenant-admin/tenant-admin.module').then(m => m.TenantAdminModule)
  },
  {
    path: '404/:message',
    component: NotFoundComponent,
  },
  {
    path: 'forbidden',
    component: ForbiddenComponent
  },
  { path: '', redirectTo: '/welcome', pathMatch: 'full' },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
