import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { ContactPersonsComponent } from './components/contact-person-list/contact-persons.component';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactPersonsResolver } from '@qro/client/contact-persons/domain';

@NgModule({
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    RouterModule.forChild([
      {
        path: '',
        component: ContactPersonsComponent,
        resolve: { contacts: ContactPersonsResolver },
      },
    ]),
  ],
})
export class ClientContactPersonsContactPersonListModule {}
