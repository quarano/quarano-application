import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { map } from 'rxjs/operators';
import { CaseDto, GetEmptyCase } from '../model/case';
import { IndexCaseEntityService } from '../data-access/index-case-entity.service';

@Injectable()
export class CaseDetailResolver implements Resolve<CaseDto> {
  constructor(private entityService: IndexCaseEntityService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<CaseDto> {
    const id = route.paramMap.get('id');
    if (id) {
      return this.entityService.getByKey(id).pipe(
        map((detail) => {
          if (!detail.caseId) {
            detail.caseId = id;
          }

          return detail;
        })
      );
    } else {
      return of(GetEmptyCase());
    }
  }
}
