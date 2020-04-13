import { IIdentifiable } from './general';
import { ContactPersonDto } from './contact-person';
import { SymptomDto } from './symptom';

export interface DiaryEntryDto extends IIdentifiable {
  bodyTemperature: number;
  symptoms: SymptomDto[];
  characteristicSymptoms: SymptomDto[];
  nonCharacteristicSymptoms: SymptomDto[];
  date: Date;
  transmittedToHealthDepartment: boolean;
  contacts: ContactPersonDto[];
}

export interface DiaryEntryModifyDto extends IIdentifiable {
  bodyTemperature: number;
  symptoms: number[];
  contacts: number[];
  date: Date;
}
