import { Link, HalResponse } from '@qro/shared/util-data-access';

export interface EncountersDto extends HalResponse {
  _embedded?: { encounters: EncounterDto[] };
}

export interface EncounterDto {
  date: string;
  firstName: string;
  lastName: string;
  _links: EncounterLinks;
}

export interface EncounterCreateDto {
  date: string;
  contact: string;
}

export interface EncounterLinks {
  contact: Link;
  self: Link;
}

export interface EncounterEntry {
  date: string;
  contactPersonId: string;
  encounter: EncounterDto;
}
