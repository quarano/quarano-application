import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { map } from 'rxjs/operators';
import { CaseDetailDto, GetEmptyCaseDetail } from '../model/case-detail';
import { HealthDepartmentService } from '../data-access/health-department.service';

@Injectable()
export class CaseDetailResolver implements Resolve<CaseDetailDto> {
  constructor(private healthDepartmentService: HealthDepartmentService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<CaseDetailDto> {
    const id = route.paramMap.get('id');
    if (id) {
      return this.healthDepartmentService.getCase(id).pipe(
        map((detail) => {
          if (!detail.caseId) {
            detail.caseId = id;
          }

          return detail;
        })
      );
    } else {
      return of(GetEmptyCaseDetail());
    }
  }
}
