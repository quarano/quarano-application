import { IIdentifiable } from '../../../../util-data-access/src/lib/models/general';

export interface SymptomDto extends IIdentifiable {
  name: string;
  characteristic: boolean;
}
