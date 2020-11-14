import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { filter, first, tap } from 'rxjs/operators';
import { AccountEntityService } from '../data-access/account-entity.service';

@Injectable()
export class AccountListResolver implements Resolve<boolean> {
  constructor(private entityService: AccountEntityService) {}

  // ToDo: Caching muss optimiert werden
  resolve(route: ActivatedRouteSnapshot): Observable<boolean> {
    return this.entityService.loaded$.pipe(
      tap((loaded) => this.entityService.load()),
      // tap((loaded) => {
      //   if (!loaded) {
      //     this.entityService.getAll();
      //   }
      // }),
      filter((loaded) => !!loaded),
      first()
    );
  }
}
