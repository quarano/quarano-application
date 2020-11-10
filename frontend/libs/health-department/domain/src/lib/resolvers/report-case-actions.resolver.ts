import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { CaseActionDto } from '../model/case-action';
import { HealthDepartmentService } from '../data-access/health-department.service';

@Injectable()
export class ReportCaseActionsResolver implements Resolve<CaseActionDto> {
  constructor(private healthDepartmentService: HealthDepartmentService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<CaseActionDto> {
    const id = route.parent.paramMap.get('id');

    if (id) {
      return this.healthDepartmentService.getCaseActions(id);
    } else {
      return of(null);
    }
  }
}
