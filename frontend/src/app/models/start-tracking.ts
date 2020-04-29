import {HalResponse} from '@models/hal-response';

export interface StartTracking extends HalResponse {
  activationCode: string;
  expirationDate: string;
  email: string;
}
