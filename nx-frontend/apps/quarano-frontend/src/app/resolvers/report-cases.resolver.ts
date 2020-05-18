import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import {ReportCaseDto} from '../models/report-case';
import {ApiService} from '../services/api.service';

@Injectable()
export class ReportCasesResolver implements Resolve<ReportCaseDto[]> {
  constructor(private apiService: ApiService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<ReportCaseDto[]> {
    return this.apiService.getCases();
  }
}
