import { IIdentifiable } from '@qro/shared/util-data-access';

export interface TrackedCaseDiaryEntryDto extends IIdentifiable {
  bodyTemperature: number;
  timeOfDay: string;
  date: Date;
  symptoms: string[];
  contacts: string[];
}
