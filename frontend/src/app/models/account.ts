import { Link } from '@models/general';
import { IIdentifiable } from './general';

export interface AccountDto extends IIdentifiable {
  lastName: string;
  firstName: string;
  username: string;
  email: string;
  roles: string[];
  _links: AccountLinks;
}

export interface AccountLinks {
  delete: Link;
  details: Link;
}
