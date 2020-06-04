import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'contact-person-list',
    pathMatch: 'full',
  },
  {
    path: 'contact-person-list',
    loadChildren: () =>
      import('@qro/client/contact-persons/contact-person-list').then(
        (m) => m.ClientContactPersonsContactPersonListModule
      ),
  },
  {
    path: 'contact-person-detail',
    loadChildren: () =>
      import('@qro/client/contact-persons/contact-person-detail').then(
        (m) => m.ClientContactPersonsContactPersonDetailModule
      ),
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class ClientContactPersonsShellModule {}
