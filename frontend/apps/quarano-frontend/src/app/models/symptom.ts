import { IIdentifiable } from './general';

export interface SymptomDto extends IIdentifiable {
  name: string;
  characteristic: boolean;
}
