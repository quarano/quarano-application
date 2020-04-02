import { ContactPersonDto } from './contact-person';
import { SymptomDto } from './symptom';

export interface DiaryEntryDto {
  bodyTemperature: number;
  id: number;
  symptoms: SymptomDto[];
  characteristicSymptoms: SymptomDto[];
  nonCharacteristicSymptoms: SymptomDto[];
  dateTime: Date;
  transmittedToHealthDepartment: boolean;
  contactPersonList: ContactPersonDto[];
}

export interface DiaryEntryModifyDto {
  bodyTemperature: number;
  id: number;
  symptoms: number[];
  contactPersonList: number[];
  dateTime: Date;
}
