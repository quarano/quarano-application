import { ClientUiForgottenContactDialogModule } from '@qro/client/ui-forgotten-contact-dialog';
import { SharedUtilTranslationModule } from '@qro/shared/util-translation';
import { ClientUiContactPersonDetailModule } from '@qro/client/ui-contact-person-detail';
import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { ContactPersonComponent } from './contact-person/contact-person.component';
import { ContactPersonResolver, ContactPersonsResolver, ClientDomainModule } from '@qro/client/domain';
import { PreventUnsavedChangesGuard } from '@qro/shared/util-forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ContactPersonsComponent } from './/contact-person-list/contact-persons.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'contact-person-list',
    pathMatch: 'full',
  },
  {
    path: 'contact-person-list',
    component: ContactPersonsComponent,
    resolve: { contacts: ContactPersonsResolver },
  },
  {
    path: 'contact-person-detail',
    children: [
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
    ],
  },
];

@NgModule({
  declarations: [ContactPersonComponent, ContactPersonsComponent],
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    SharedUiButtonModule,
    FormsModule,
    ReactiveFormsModule,
    ClientUiContactPersonDetailModule,
    ClientDomainModule,
    RouterModule.forChild(routes),
    SharedUtilTranslationModule,
    ClientUiForgottenContactDialogModule,
  ],
})
export class ClientFeatureContactPersonsModule {}
