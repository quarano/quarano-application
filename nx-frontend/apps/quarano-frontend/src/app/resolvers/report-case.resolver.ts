import {Observable, of} from 'rxjs';
import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {map} from 'rxjs/operators';
import {ApiService} from '../services/api.service';
import {CaseDetailDto} from '../models/case-detail';

@Injectable()
export class ReportCaseResolver implements Resolve<CaseDetailDto> {
  constructor(private apiService: ApiService) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<CaseDetailDto> {
    const id = route.paramMap.get('id');

    if (id) {
      return this.apiService.getCase(id).pipe(
        map((detail) => {
          if (!detail.caseId) {
            detail.caseId = id;
          }

          return detail;
        }));
    } else {
      return of(null);
    }
  }
}
