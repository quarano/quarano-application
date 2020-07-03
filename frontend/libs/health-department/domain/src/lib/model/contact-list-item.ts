import { HalResponse } from '@qro/shared/util-data-access';
import { CaseStatus } from './case-detail';

export interface ContactListItemDto extends HalResponse {
  contactDates: Date[];
  caseStatusLabel: string;
  lastName: string;
  firstName: string;
  isHealthStaff: boolean;
  contactId: string;
  hasPreExistingConditions: boolean;
  isSenior: boolean;
  caseId: string;
  caseType: string;
  caseTypeLabel: string;
}
