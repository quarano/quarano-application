import { IIdentifiable, SelfLink } from '@qro/shared/util-data-access';

export interface StoredContactLocationDto extends IIdentifiable {
  name: string;
  street: string;
  houseNumber: string;
  zipCode: string;
  city: string;
  contactPerson: string;
  startTime: string;
  endTime: string;
  notes: string;
  _links: SelfLink;
}

export interface TransientContactLocationDto {
  name: string;
  street: string;
  houseNumber: string;
  zipCode: string;
  city: string;
  contactPerson: string;
  startTime: string;
  endTime: string;
  notes: string;
}
