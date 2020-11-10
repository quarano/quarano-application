import { AccountService } from '../data-access/account.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { map } from 'rxjs/operators';
import { AccountDto } from '../model/account';

@Injectable()
export class AccountListResolver implements Resolve<AccountDto[]> {
  constructor(private apiService: AccountService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<AccountDto[]> {
    return this.apiService.getAccountList().pipe(map((users) => users?.accounts || []));
  }
}
