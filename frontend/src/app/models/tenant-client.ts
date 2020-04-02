import {Client} from './client';
import {DiaryEntryDto} from './diary-entry';

export interface TenantClient extends Client {
  diaryEntires: Array<DiaryEntryDto>;
  monitoringStatus: string;
  monitoringMessage: string;
}
