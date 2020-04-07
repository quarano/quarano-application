import { IIdentifiable } from './general';

export interface ContactPersonDto extends IIdentifiable {
  firstname: string;
  surename: string;
  phone: string;
  mobilePhone: string;
  email: string;
  street: string;
  houseNumber: string;
  zipCode: string;
  city: string;
  remark: string;
  identificationHint: string;
  isHealthStuff: boolean | null;
  isSenior: boolean | null;
  hasPreExistingConditions: boolean | null;
}
