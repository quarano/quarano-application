import { Link } from '@qro/shared/util-data-access';

export interface ZipCodeErrorDto {
  zipCode: {
    message: string;
    institution: {
      name: string;
      department: string;
      street: string;
      city: string;
      zipCode: string;
      fax: string;
      phone: string;
      email: string;
    };
  };
  _links: {
    confirm: Link;
  };
}
