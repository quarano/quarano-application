import { HalResponse } from '@qro/shared/util-data-access';

export interface AccountDto extends HalResponse {
  accountId: string;
  lastName: string;
  firstName: string;
  username: string;
  email: string;
  roles: string[];
}

export interface AccountListDto {
  accounts: AccountDto[];
}
