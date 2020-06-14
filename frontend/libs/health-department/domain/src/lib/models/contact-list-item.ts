import { HalResponse } from '@qro/shared/util-data-access';
import { CaseStatus } from './case-detail';

export interface ContactListItemDto extends HalResponse {
  descendingSortedContactDates: Date[];
  contactStatus: CaseStatus;
  lastName: string;
  firstName: string;
  isHealthStaff: boolean;
  contactId: string;
  hasPreExistingConditions: boolean;
  isSenior: boolean;
}
