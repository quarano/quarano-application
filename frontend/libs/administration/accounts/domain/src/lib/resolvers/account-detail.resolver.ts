import { AccountService } from './../services/account.service';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { AccountDto } from '../models/account';

@Injectable()
export class AccountDetailResolver implements Resolve<AccountDto> {
  constructor(private apiService: AccountService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<AccountDto> {
    const id = route.paramMap.get('id');

    if (id) {
      return this.apiService.getAccountDetail(id);
    } else {
      return of(
        {
          accountId: null,
          firstName: null,
          lastName: null,
          username: null,
          _links: null,
          email: null,
          roles: []
        });
    }
  }
}
