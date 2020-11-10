import { HalResponse } from '@qro/shared/util-data-access';

export interface CaseSearchItem extends HalResponse {
  firstName: string;
  lastName: string;
  dateOfBirth: string;
}
