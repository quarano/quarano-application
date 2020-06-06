import { HalResponse } from '@qro/shared/util-data-access';
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
  status?: CaseStatus;
  infected: boolean;
  caseTypeLabel: string;
  extReferenceNumber: string;
  indexContacts?: ContactDto[];
}

export enum CaseStatus {
  Angelegt = 'angelegt',
  InRegistrierung = 'in Registrierung',
  RegistrierungAbgeschlossen = 'Registrierung abgeschlossen',
  InNachverfolgung = 'in Nachverfolgung',
  Abgeschlossen = 'abgeschlossen',
}
