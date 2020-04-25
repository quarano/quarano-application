export interface ReportCaseDto {
  dateOfBirth: Date;
  firstName: string;
  lastName: string;
  zipCode?: string;
  email: string;
  phone: string;
  enrollmentCompleted: boolean;
  caseType: ClientType;
  medicalStaff?: boolean;
  quarantineEnd: Date;
  quarantineStart: Date;
  status: string;
}

export enum ClientType {
  INDEX = 'INDEX',
  CONTACT = 'CONTACT'
}
