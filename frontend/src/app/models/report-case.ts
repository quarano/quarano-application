export interface ReportCaseDto {
  dateOfBirth: Date;
  firstName: string;
  lastName: string;
  zipCode?: string;
  email: string;
  enrollmentCompleted: boolean;
  caseType: 'index' | 'contact';
  medicalStaff?: boolean;
  quarantine?: {
    from: Date;
    to: Date;
  };
}
