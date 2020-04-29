import { SelfLink } from './general';
import { ClientType } from './report-case';

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
  _links: SelfLink;
}

export enum Alert {
  FIRST_CHARACTERISTIC_SYMPTOM = 'FIRST_CHARACTERISTIC_SYMPTOM',
  DIARY_ENTRY_MISSING = 'DIARY_ENTRY_MISSING',
  INCREASED_TEMPERATURE = 'INCREASED_TEMPERATURE',
  MISSING_DETAILS_INDEX = 'MISSING_DETAILS_INDEX',
  MISSING_DETAILS_CONTACT = 'MISSING_DETAILS_CONTACT',
  QUARANTINE_END = 'QUARANTINE_END',
  ENCOUNTER_IN_QUARANTINE = 'ENCOUNTER_IN_QUARANTINE',
  INITIAL_CALL_OPEN_INDEX = 'INITIAL_CALL_OPEN_INDEX',
  INITIAL_CALL_OPEN_CONTACT = 'INITIAL_CALL_OPEN_CONTACT'
}
