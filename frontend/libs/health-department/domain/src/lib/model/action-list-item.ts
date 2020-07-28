import { Link } from '@qro/shared/util-data-access';
import { Alert } from '../enums/alert';
import { ClientType } from '@qro/auth/api';
import { CaseSearchItem } from './case-search-item';

export interface ActionListItemDto {
  name: string;
  priority: number;
  caseType: ClientType;
  dateOfBirth: Date;
  firstName: string;
  lastName: string;
  phone: string;
  email: string;
  caseId: string;
  quarantineEnd: Date;
  quarantineStart: Date;
  alerts: Alert[];
  status: string;
  caseTypeLabel: string;
  createdAt: Date;
  extReferenceNumber: string;
  _links: { self: Link; originCases?: Link };
  originCases?: CaseSearchItem[];
}
