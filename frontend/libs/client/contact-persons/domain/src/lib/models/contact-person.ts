import { IIdentifiable, SelfLink } from '../../../../../../../apps/quarano-frontend/src/app/models/general';

export interface ContactPersonDto extends IIdentifiable {
  firstName: string;
  lastName: string;
  phone: string;
  mobilePhone: string;
  email: string;
  street: string;
  houseNumber: string;
  zipCode: string;
  city: string;
  remark: string;
  identificationHint: string;
  isHealthStaff: boolean | null;
  isSenior: boolean | null;
  hasPreExistingConditions: boolean | null;
  _links: SelfLink;
}

export interface ContactPersonModifyDto {
  firstName: string;
  lastName: string;
  phone: string;
  mobilePhone: string;
  email: string;
  street: string;
  houseNumber: string;
  zipCode: string;
  city: string;
  remark: string;
  identificationHint: string;
  isHealthStaff: boolean | null;
  isSenior: boolean | null;
  hasPreExistingConditions: boolean | null;
}
