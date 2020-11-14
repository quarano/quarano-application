import { AccountDto } from './../model/account';
import { Injectable } from '@angular/core';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { Observable, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';

export const ACCOUNT_FEATURE_KEY = 'Account';

const emptyAccount: AccountDto = {
  accountId: null,
  firstName: null,
  lastName: null,
  username: null,
  _links: {},
  email: null,
  roles: [],
};

@Injectable()
export class AccountEntityService extends EntityCollectionServiceBase<AccountDto> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super(ACCOUNT_FEATURE_KEY, serviceElementsFactory);
  }

  public loadOneFromStore(id: string): Observable<AccountDto> {
    if (id) {
      return this.entityMap$.pipe(
        switchMap((entities) => {
          const loadedAccount = entities[id];
          if (loadedAccount) {
            return of(loadedAccount);
          } else {
            return this.getByKey(id);
          }
        })
      );
    } else {
      return of(emptyAccount);
    }
  }
}
