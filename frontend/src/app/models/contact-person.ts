import { IIdentifiable } from './general';

export interface ContactPersonDto extends IIdentifiable {
  firstname: string;
  surename: string;
  phone: string;
  email: string;
}
