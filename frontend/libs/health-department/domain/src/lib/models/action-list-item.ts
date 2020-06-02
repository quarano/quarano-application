import { SelfLink } from '../../../../../shared/util-data-access/src/lib/models/general';
import { Alert } from '../enums/alert';
import { ClientType } from '../enums/client-type';

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
  _links: SelfLink;
}
