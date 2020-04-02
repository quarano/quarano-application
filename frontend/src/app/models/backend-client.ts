import {Client} from './client';
import {FirstQuery} from './first-query';

export interface BackendClient extends Client {
  comments: Array<FirstQuery>;
  clientCode: string;
}
