import { AccountEntityService } from './../data-access/account-entity.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { AccountDto } from '../model/account';

@Injectable()
export class AccountDetailResolver implements Resolve<AccountDto> {
  constructor(private entityService: AccountEntityService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<AccountDto> {
    const id = route.paramMap.get('id');
    return this.entityService.getByKey(id);
  }
}
