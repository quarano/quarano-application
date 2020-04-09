export interface Client {
  clientCode?: string;
  clientId: string;
  surename: string;
  firstname: string;
  phone: string;
  zipCode: string;
  street?: string;
  city?: string;
  email?: string;
  mobilephone?: string;
  infected: boolean;
  completedPersonalData: boolean;
  completedQuestionnaire: boolean;
  completedContactRetro: boolean;
  quarantineStartDateTime?: Date;
  quarantineEndDateTime?: Date;
}
