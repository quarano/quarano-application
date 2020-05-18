import {ClientDto} from './client';

export interface BackendClient extends ClientDto {
  clientCode: string;
}
