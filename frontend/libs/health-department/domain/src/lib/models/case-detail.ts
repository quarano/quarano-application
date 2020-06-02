import { HalResponse } from '../../../../../shared/util-data-access/src/lib/models/hal-response';
import { CaseCommentDto } from './case-comment';
import { ContactDto } from './contact';

export interface CaseDetailDto extends HalResponse {
  caseId?: string;
  firstName: string;
  lastName: string;
  testDate?: Date;
  quarantineStartDate: Date;
  quarantineEndDate: Date;
  street?: string;
  houseNumber?: string;
  city?: string;
  zipCode?: string;
  mobilePhone?: string;
  phone?: string;
  email?: string;
  dateOfBirth: Date;
  comments?: CaseCommentDto[];
  status?: string;
  infected: boolean;
  caseTypeLabel: string;
  extReferenceNumber: string;

  indexContacts?: ContactDto[];
}
