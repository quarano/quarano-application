import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { map, tap } from 'rxjs/operators';
import { CaseDto, getEmptyCase } from '../model/case';
import { CaseEntityService } from '../data-access/case-entity.service';

@Injectable()
export class CaseDetailResolver implements Resolve<CaseDto> {
  constructor(private entityService: CaseEntityService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<CaseDto> {
    const id = route.paramMap.get('id');
    if (id) {
      return this.entityService.getByKey(id).pipe(
        map((detail) => {
          console.log(detail);
          if (!detail.caseId) {
            detail.caseId = id;
          }

          return detail;
        })
      );
    } else {
      return of(getEmptyCase());
    }
  }
}
