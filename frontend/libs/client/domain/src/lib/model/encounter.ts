import { Link, HalResponse } from '@qro/shared/util-data-access';

export interface EncountersDto extends HalResponse {
  _embedded?: { encounters: EncounterDto[] };
}

export interface EncounterDto {
  date: string;
  firstName: string;
  lastName: string;
  locationName: string;
  _links: EncounterLinks;
}

export interface EncounterCreateDto {
  date: string;
  contact: string;
  location: string;
}

export interface EncounterLinks {
  contact: Link;
  self: Link;
  diaryEntry: Link;
  location: Link;
}

export interface EncounterEntry {
  date: string;
  contactPersonId: string;
  encounter: EncounterDto;
  locationId: string;
  from: string;
  to: string;
}
