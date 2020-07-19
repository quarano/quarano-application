import { AuthDomainModule } from '@qro/auth/api';
import { IndexCaseDataService } from './data-access/index-case-data.service';
import { IndexCaseEntityService, CASE_FEATURE_KEY } from './data-access/index-case-entity.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportCaseActionsResolver } from './resolvers/report-case-actions.resolver';
import { CaseDetailResolver } from './resolvers/case-detail.resolver';
import { ContactCaseCaseListResolver } from './resolvers/contact-case-case-list.resolver';
import { ContactCaseActionListResolver } from './resolvers/contact-case-action-list.resolver';
import { IndexCaseActionListResolver } from './resolvers/index-case-action-list.resolver';
import { IndexCaseCaseListResolver } from './resolvers/index-case-case-list.resolver';
import { EntityDataService, EntityDefinitionService, EntityMetadataMap } from '@ngrx/data';
import { HttpClientModule } from '@angular/common/http';
import { CaseDto } from './model/case';

const entityMetadata: EntityMetadataMap = {
  Case: {
    selectId: (item: CaseDto) => item.caseId,
  },
};

@NgModule({
  imports: [CommonModule, HttpClientModule, AuthDomainModule],
  providers: [
    ReportCaseActionsResolver,
    CaseDetailResolver,
    ContactCaseActionListResolver,
    ContactCaseCaseListResolver,
    IndexCaseActionListResolver,
    IndexCaseCaseListResolver,
    IndexCaseEntityService,
    IndexCaseDataService,
  ],
})
export class HealthDepartmentDomainModule {
  constructor(
    private eds: EntityDefinitionService,
    private entityDataService: EntityDataService,
    private indexCaseDataService: IndexCaseDataService
  ) {
    eds.registerMetadataMap(entityMetadata);
    entityDataService.registerService(CASE_FEATURE_KEY, indexCaseDataService);
  }
}
