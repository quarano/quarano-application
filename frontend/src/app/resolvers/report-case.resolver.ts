import {ApiService} from '@services/api.service';
import {Observable, of} from 'rxjs';
import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {CaseDetailDto} from '@models/case-detail';
import {map} from 'rxjs/operators';

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
