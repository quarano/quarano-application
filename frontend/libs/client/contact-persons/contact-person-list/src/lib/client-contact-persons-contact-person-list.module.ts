import { SharedUiAlertModule } from '@qro/shared/ui-alert';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { ContactPersonsComponent } from './components/contact-person-list/contact-persons.component';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactPersonsResolver } from '@qro/client/contact-persons/domain';

@NgModule({
  declarations: [ContactPersonsComponent],
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    SharedUiAlertModule,
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
