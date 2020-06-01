import {
  IIdentifiable,
  CreateLink,
  SelfLink,
  Link,
} from '../../../../../../../apps/quarano-frontend/src/app/models/general';

export interface DiaryEntryDto extends IIdentifiable {
  bodyTemperature: number;
  slot: SlotDto;
  symptoms: DiaryEntrySymptomDto[];
  characteristicSymptoms: DiaryEntrySymptomDto[];
  nonCharacteristicSymptoms: DiaryEntrySymptomDto[];
  date: Date;
  contacts: DiaryEntryContactDto[];
  _links: DiaryEntryLinks;
  reportedAt: string;
}

export interface DiaryEntryContactDto extends IIdentifiable {
  firstName: string;
  lastName: string;
  _links: SelfLink;
}

export interface DiaryEntrySymptomDto extends IIdentifiable {
  name: string;
  characteristic: boolean;
}

export interface DiaryEntryModifyDto extends IIdentifiable {
  bodyTemperature: number;
  symptoms: number[];
  contacts: number[];
  date: string;
  timeOfDay: string;
}

export interface SlotDto {
  date: string;
  timeOfDay: string;
}

export interface DiaryDto extends DiaryEntryCreateLink {
  _embedded: DiaryEntryListDto;
}

export interface DiaryEntryListDto {
  entries: DiaryListItemDto[];
}

export interface DiaryListItemDto {
  date: string;
  evening: DiaryEntryCreateLink | DiaryEntryListItemDto;
  morning: DiaryEntryCreateLink | DiaryEntryListItemDto;
}

export interface DiaryEntryListItemDto {
  id: string;
  date: Date;
  contacts: DiaryEntryContactDto[];
  bodyTemperature: number;
  symptoms: DiaryEntrySymptomDto[];
  _links: DiaryEntryLinks;
}

export interface DiaryEntryLinks {
  edit: Link;
  self: Link;
}

export interface DiaryEntryCreateLink {
  _links: CreateLink;
}
