import { ContactPersonComponent } from './contact-person/contact-person.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContactPersonsComponent } from './contact-persons.component';
import { ContactPersonsResolver } from '@qro/client/contact-persons/domain';
import { ContactPersonResolver } from '../../resolvers/contact-person.resolver';
import { PreventUnsavedChangesGuard } from '@qro/shared/util';

const routes: Routes = [
  {
    path: '',
    component: ContactPersonsComponent,
    resolve: { contacts: ContactPersonsResolver },
  },
  {
    path: 'edit/:id',
    component: ContactPersonComponent,
    resolve: { contactPerson: ContactPersonResolver },
    canDeactivate: [PreventUnsavedChangesGuard],
  },
  {
    path: 'new',
    component: ContactPersonComponent,
    resolve: { contactPerson: ContactPersonResolver },
    canDeactivate: [PreventUnsavedChangesGuard],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ContactRoutingModule {}
