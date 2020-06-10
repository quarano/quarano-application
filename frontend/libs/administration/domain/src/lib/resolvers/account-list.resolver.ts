import { AccountService } from '../services/account.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { map } from 'rxjs/operators';
import { AccountDto } from '../models/account';

@Injectable()
export class AccountListResolver implements Resolve<AccountDto[]> {
  constructor(private apiService: AccountService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<AccountDto[]> {
    return this.apiService.getAccountList().pipe(map((users) => users?.accounts || []));
  }
}
