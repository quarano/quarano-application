import { SharedUiButtonModule } from '@qro/shared/ui-button';
import { SharedUiAlertModule } from '@qro/shared/ui-alert';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { ContactPersonResolver } from '@qro/client/contact-persons/domain';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactPersonComponent } from './components/contact-person/contact-person.component';
import { PreventUnsavedChangesGuard } from '@qro/shared/util';
import { ContactPersonFormComponent } from './components/contact-person-form/contact-person-form.component';
import { ContactPersonDialogComponent } from './components/contact-person-dialog/contact-person-dialog.component';

const routes: Routes = [
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
  declarations: [ContactPersonComponent, ContactPersonDialogComponent, ContactPersonFormComponent],
  entryComponents: [ContactPersonDialogComponent],
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    SharedUiAlertModule,
    SharedUiButtonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
  ],
})
export class ClientContactPersonsContactPersonDetailModule {}
