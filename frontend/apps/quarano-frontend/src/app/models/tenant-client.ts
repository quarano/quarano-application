import { ClientDto } from '../../../../../libs/client/domain/src/lib/models/client';
import { DiaryEntryDto } from './diary-entry';

export interface TenantClientDto extends ClientDto {
  diaryEntires: Array<DiaryEntryDto>;
  monitoringStatus: string;
  monitoringMessage: string;
}
