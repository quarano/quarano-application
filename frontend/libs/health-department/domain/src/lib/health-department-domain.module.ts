import { AuthDomainModule } from '@qro/auth/api';
import { CaseDataService } from './data-access/case-data.service';
import { CaseEntityService, CASE_FEATURE_KEY } from './data-access/case-entity.service';
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
import { CaseDto, sortByLastName, caseTypeFilter } from './model/case';
import { SharedUtilDateModule } from '@qro/shared/util-date';
import { TrackedCaseDiaryEntriesResolver } from './resolvers/tracked-case-diary-entries.resolver';

const entityMetadata: EntityMetadataMap = {
  Case: {
    selectId: (item: CaseDto) => item.caseId,
    sortComparer: sortByLastName,
    filterFn: caseTypeFilter,
  },
};

@NgModule({
  imports: [CommonModule, HttpClientModule, AuthDomainModule, SharedUtilDateModule],
  providers: [
    ReportCaseActionsResolver,
    CaseDetailResolver,
    ContactCaseActionListResolver,
    ContactCaseCaseListResolver,
    IndexCaseActionListResolver,
    IndexCaseCaseListResolver,
    CaseEntityService,
    CaseDataService,
    TrackedCaseDiaryEntriesResolver,
  ],
})
export class HealthDepartmentDomainModule {
  constructor(
    private eds: EntityDefinitionService,
    private entityDataService: EntityDataService,
    private indexCaseDataService: CaseDataService
  ) {
    eds.registerMetadataMap(entityMetadata);
    entityDataService.registerService(CASE_FEATURE_KEY, indexCaseDataService);
  }
}
