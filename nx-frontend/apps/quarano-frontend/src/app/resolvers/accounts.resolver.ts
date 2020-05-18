import { ApiService } from '@services/api.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { AccountDto } from '@models/account';
import { map } from 'rxjs/operators';

@Injectable()
export class AccountsResolver implements Resolve<AccountDto[]> {
  constructor(private apiService: ApiService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<AccountDto[]> {
    return this.apiService.getHealthDepartmentUsers().pipe(map(users => users?.accounts || []));
  }
}
