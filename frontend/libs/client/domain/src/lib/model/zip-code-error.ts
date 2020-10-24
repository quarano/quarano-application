import { HalResponse } from '@qro/shared/util-data-access';

export interface ZipCodeErrorDto extends HalResponse {
  zipCode: {
    message: string;
    institution: HealthDepartmentDto;
  };
}

export interface HealthDepartmentDto {
  name: string;
  department: string;
  street: string;
  city: string;
  zipCode: string;
  fax: string;
  phone: string;
  email: string;
}
