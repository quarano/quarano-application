import {HalResponse} from './hal-response';

export interface StartTracking extends HalResponse {
  activationCode: string;
  expirationDate: string;
  email: string;
}
