export interface Client {
  clientCode?: string;
  clientId: number;
  lastName: string;
  firstName: string;
  phone?: string;
  zipCode: string;
  street?: string;
  city?: string;
  email?: string;
  mobilePhone?: string;
  infected: boolean;
  quarantineStartDateTime?: Date;
  quarantineEndDateTime?: Date;
  houseNumber: string;
  dateOfBirth: Date;
  type: ClientType;
}

export enum ClientType {
  INDEX_CASE, CONTACT_CASE
}
