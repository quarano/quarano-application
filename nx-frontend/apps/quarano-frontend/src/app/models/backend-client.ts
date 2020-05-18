import { ClientDto } from './client';
import { QuestionnaireDto } from './first-query';

export interface BackendClient extends ClientDto {
  clientCode: string;
}
