import { IIdentifiable, Link } from './general';
import { ContactPersonDto } from './contact-person';
import { SymptomDto } from './symptom';

export interface DiaryEntryDto extends IIdentifiable {
  bodyTemperature: number;
  symptoms: SymptomDto[];
  characteristicSymptoms: SymptomDto[];
  nonCharacteristicSymptoms: SymptomDto[];
  date: Date;
  contacts: ContactPersonDto[];
  _links: DiaryEntryLinks;
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
