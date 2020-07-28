import { CaseSearchItem } from '@qro/health-department/domain';
import { HalResponse } from '@qro/shared/util-data-access';
import { ClientType } from '@qro/auth/api';

export interface CaseListItemDto extends HalResponse {
  dateOfBirth: Date;
  firstName: string;
  lastName: string;
  zipCode?: string;
  email: string;
  phone: string;
  enrollmentCompleted: boolean;
  caseType: ClientType;
  medicalStaff?: boolean;
  quarantineEnd: Date;
  quarantineStart: Date;
  status: string;
  caseId: string;
  caseTypeLabel: string;
  createdAt: Date;
  extReferenceNumber: string;
  originCases: CaseSearchItem[];
}
