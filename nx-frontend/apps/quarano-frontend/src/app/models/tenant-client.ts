import { ClientDto } from './client';
import { DiaryEntryDto } from './diary-entry';

export interface TenantClientDto extends ClientDto {
  diaryEntires: Array<DiaryEntryDto>;
  monitoringStatus: string;
  monitoringMessage: string;
}
