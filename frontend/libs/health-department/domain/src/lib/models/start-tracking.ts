import { HalResponse } from '@qro/shared/util-data-access';

export interface StartTracking extends HalResponse {
  activationCode: string;
  expirationDate: string;
  email: string;
}
