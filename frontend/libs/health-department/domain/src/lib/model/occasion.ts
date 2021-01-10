export interface OccasionDto {
  id: string;
  start: Date;
  end: Date;
  title: string;
  address: Address;
  additionalInformation: string;
  contactPerson: string;
  visitorGroups: string;
  occasionCode: string;
  trackedCaseId: string;
}

interface Address {
  street: string;
  houseNumber: number;
  city: string;
  zipCode: number;
}
