import { IIdentifiable, Link } from './general';
import { ContactPersonLinks } from './contact-person';

export interface DiaryEntryDto extends IIdentifiable {
  bodyTemperature: number;
  symptoms: DiaryEntrySymptomDto[];
  characteristicSymptoms: DiaryEntrySymptomDto[];
  nonCharacteristicSymptoms: DiaryEntrySymptomDto[];
  date: Date;
  contacts: DiaryEntryContactDto[];
  _links: DiaryEntryLinks;
}

export interface DiaryEntryContactDto extends IIdentifiable {
  firstName: string;
  lastName: string;
  _links: ContactPersonLinks;
}

export interface DiaryEntrySymptomDto extends IIdentifiable {
  name: string;
  characteristic: boolean;
}

export interface DiaryEntryLinks {
  self: Link;
}

export interface DiaryEntryModifyDto extends IIdentifiable {
  bodyTemperature: number;
  symptoms: number[];
  contacts: number[];
  date: Date;
}
