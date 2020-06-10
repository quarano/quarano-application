import { AdministrationDomainModule } from '@qro/administration/domain';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { AccountListResolver } from '@qro/administration/domain';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountListComponent } from './account-list/account-list.component';

@NgModule({
  declarations: [AccountListComponent],
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    NgxDatatableModule,
    AdministrationDomainModule,
    RouterModule.forChild([
      {
        path: '',
        pathMatch: 'full',
        component: AccountListComponent,
        resolve: { accounts: AccountListResolver },
      },
    ]),
  ],
})
export class AdministrationFeatureAccountListModule {}
