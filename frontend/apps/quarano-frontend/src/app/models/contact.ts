export class ContactDto {
  caseId: string;
  firstName: string;
  lastName: string;
  dateOfBirth: Date;

  contactAt: Date;

  isHealthStaff: boolean;
  isSenior: boolean;
  hasPreExistingConditions: boolean;
  identificationHint: string;
}
