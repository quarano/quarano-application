import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { CaseActionDto } from '../models/case-action';
import { HealthDepartmentService } from '../services/health-department.service';

@Injectable()
export class ReportCaseActionsResolver implements Resolve<CaseActionDto> {
  constructor(private healthDepartmentService: HealthDepartmentService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<CaseActionDto> {
    const id = route.paramMap.get('id');

    if (id) {
      return this.healthDepartmentService.getCaseActions(id);
    } else {
      return of(null);
    }
  }
}
