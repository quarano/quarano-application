import { HalResponse } from '@qro/shared/util-data-access';

export interface ContactListItemDto extends HalResponse {
  contactDates: Date[];
  caseStatusLabel: string;
  lastName: string;
  firstName: string;
  isHealthStaff: boolean;
  contactId: string;
  hasPreExistingConditions: boolean;
  remark: string;
  isSenior: boolean;
  identificationHint: string;
  caseId: string;
  caseType: string;
  caseTypeLabel: string;
}
