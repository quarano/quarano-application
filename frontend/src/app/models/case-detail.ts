export interface CaseDetailDto {
  caseId?: string;

  firstName: string;
  lastName: string;

  testDate?: Date;

  quarantineStartDate: Date;
  quarantineEndDate: Date;

  street?: string;
  houseNumber?: string;
  city?: string;
  zipCode?: string;

  mobilePhone?: string;
  phone?: string;

  email?: string;

  dateOfBirth: Date;

  comment?: string;

  status?: string;

  infected: boolean;
}
