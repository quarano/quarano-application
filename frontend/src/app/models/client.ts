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
  mobilephone?: string;
  infected: boolean;
  quarantineStartDateTime?: Date;
  quarantineEndDateTime?: Date;
  houseNumber: string;
  dateOfBirth: Date;
}
