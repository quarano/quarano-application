import { IIdentifiable } from '@qro/shared/util-data-access';

export interface TrackedCaseDiaryEntryDto extends IIdentifiable {
  bodyTemperature: number;
  slot: TrackedCaseDiaryEntrySlotDto;
  symptoms: TrackedCaseDiaryEntrySymptomDto[];
  contacts: TrackedCaseDiaryEntryContactDto[];
}

export interface TrackedCaseDiaryEntrySlotDto {
  date: Date;
  timeOfDay: string;
}

export interface TrackedCaseDiaryEntrySymptomDto extends IIdentifiable {
  name: string;
}

export interface TrackedCaseDiaryEntryContactDto extends IIdentifiable {
  firstName: string;
  lastName: string;
}
