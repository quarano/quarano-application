import { SharedUiErrorModule } from '@qro/shared/ui-error';
import { AuthDomainModule } from '@qro/auth/api';
import { HttpClientModule } from '@angular/common/http';
import { AccountDto } from './model/account';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountDetailResolver } from './resolvers/account-detail.resolver';
import { AccountListResolver } from './resolvers/account-list.resolver';
import { EntityDataService, EntityDefinitionService, EntityMetadataMap } from '@ngrx/data';
import { AccountEntityService, ACCOUNT_FEATURE_KEY } from './data-access/account-entity.service';
import { AccountDataService } from './data-access/account-data.service';

const entityMetadata: EntityMetadataMap = {
  Account: {
    selectId: (item: AccountDto) => item.accountId,
  },
};

@NgModule({
  imports: [CommonModule, HttpClientModule, AuthDomainModule, SharedUiErrorModule],
  providers: [AccountEntityService, AccountDataService, AccountListResolver, AccountDetailResolver],
})
export class AdministrationDomainModule {
  constructor(
    private eds: EntityDefinitionService,
    private entityDataService: EntityDataService,
    private accountDataService: AccountDataService
  ) {
    eds.registerMetadataMap(entityMetadata);
    entityDataService.registerService(ACCOUNT_FEATURE_KEY, accountDataService);
  }
}
