import { Link } from './general';
import {HalResponse} from './hal-response';


export interface EncountersDto extends HalResponse{
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
