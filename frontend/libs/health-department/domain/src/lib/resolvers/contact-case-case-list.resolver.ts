import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { IndexCaseEntityService } from '../data-access/index-case-entity.service';
import { tap, filter, first } from 'rxjs/operators';

@Injectable()
export class ContactCaseCaseListResolver implements Resolve<boolean> {
  constructor(private entityService: IndexCaseEntityService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<boolean> {
    return this.entityService.loaded$.pipe(
      tap((loaded) => {
        if (!loaded) {
          this.entityService.getAll();
        }
      }),
      filter((loaded) => !!loaded),
      first()
    );
  }
}
