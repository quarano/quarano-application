import {Observable, of} from 'rxjs';
import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {HealthDepartmentService} from "../data-access/health-department.service";
import {TrackedCaseDiaryEntryDto} from "@qro/health-department/domain";

@Injectable()
export class TrackedCaseDiaryEntriesResolver implements Resolve<TrackedCaseDiaryEntryDto[]> {
  constructor(private healthDepartmentService: HealthDepartmentService) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<TrackedCaseDiaryEntryDto[]> {
    const id = route.paramMap.get('id');
    if (id) {
      return this.healthDepartmentService.getCaseDiaryEntries(id);
    } else {
      return of(null);
    }
  }
}
