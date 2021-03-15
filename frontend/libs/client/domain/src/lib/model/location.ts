import { HalResponse, IIdentifiable } from '@qro/shared/util-data-access';

export interface LocationDto extends IIdentifiable, HalResponse {
  name: string;
  contactPerson: LocationContactDto;
  street: string;
  houseNumber: string;
  zipCode: string;
  city: string;
  comment: string;
}

export interface LocationContactDto {
  contactPersonName: string;
  contactPersonPhone: string;
  contactPersonEmail: string;
}
