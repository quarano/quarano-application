import { IIdentifiable } from '../../../../../../apps/quarano-frontend/src/app/models/general';

export interface SymptomDto extends IIdentifiable {
  name: string;
  characteristic: boolean;
}
