import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { IndexCaseService } from '../data-access/index-case.service';
import { CaseListItemDto } from '../model/case-list-item';

@Injectable()
export class IndexCaseCaseListResolver implements Resolve<CaseListItemDto[]> {
  constructor(private apiService: IndexCaseService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<CaseListItemDto[]> {
    return this.apiService.getCaseList();
  }
}
