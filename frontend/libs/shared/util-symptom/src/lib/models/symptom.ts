import { IIdentifiable } from '@qro/shared/util-data-access';

export interface SymptomDto extends IIdentifiable {
  name: string;
  characteristic: boolean;
}
