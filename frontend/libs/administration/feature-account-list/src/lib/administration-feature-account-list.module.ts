import { DeleteButtonComponent } from './account-list/delete-button.component';
import { SharedUiAgGridModule } from '@qro/shared/ui-ag-grid';
import { AdministrationDomainModule } from '@qro/administration/domain';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { AccountListResolver } from '@qro/administration/domain';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountListComponent } from './account-list/account-list.component';

@NgModule({
  declarations: [AccountListComponent, DeleteButtonComponent],
  imports: [
    CommonModule,
    SharedUiMaterialModule,
    SharedUiAgGridModule,
    AdministrationDomainModule,
    RouterModule.forChild([
      {
        path: '',
        pathMatch: 'full',
        component: AccountListComponent,
        resolve: { accountsLoaded: AccountListResolver },
      },
    ]),
  ],
})
export class AdministrationFeatureAccountListModule {}
