export interface Client {
  clientCode?: string;
  clientId: number;
  surename: string;
  firstname: string;
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
