import { Link } from "../../../../../../../apps/quarano-frontend/src/app/models/general";

export interface AccountDto {
  accountId: string;
  lastName: string;
  firstName: string;
  username: string;
  email: string;
  roles: string[];
  _links: AccountLinks;
}

export interface AccountListDto {
  accounts: AccountDto[];
}

export interface AccountLinks {
  delete: Link;
  self: Link;
}
