import { HalResponse } from '../../../../../shared/util-data-access/src/lib/models/hal-response';

export interface StartTracking extends HalResponse {
  activationCode: string;
  expirationDate: string;
  email: string;
}
