import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { CaseEntityService } from '../data-access/case-entity.service';
import { tap, filter, first } from 'rxjs/operators';
import { CaseType } from '@qro/auth/domain';

@Injectable()
export class ContactCaseCaseListResolver implements Resolve<boolean> {
  constructor(private entityService: CaseEntityService) {}

  // ToDo: Caching muss optimiert werden
  resolve(route: ActivatedRouteSnapshot): Observable<boolean> {
    return this.entityService.loaded$.pipe(
      // tap((loaded) => {
      //   if (!loaded) {
      //     this.entityService.getAll();
      //   }
      // }),
      tap((loaded) => this.entityService.load()),
      filter((loaded) => !!loaded),
      tap((loaded) => this.entityService.setFilter(CaseType.Contact)),
      first()
    );
  }
}
