import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import {CaseActionDto} from '../models/case-action';
import {ApiService} from '../services/api.service';

@Injectable()
export class ReportCaseActionsResolver implements Resolve<CaseActionDto> {
  constructor(private apiService: ApiService) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<CaseActionDto> {
    const id = route.paramMap.get('id');

    if (id) {
      return this.apiService.getCaseActions(id);
    } else {
      return of(null);
    }
  }
}
