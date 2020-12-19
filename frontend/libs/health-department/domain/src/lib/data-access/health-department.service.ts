import { CaseDto } from './../model/case';
import { API_URL, Link } from '@qro/shared/util-data-access';
import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CaseActionDto } from '../model/case-action';
import { distinctUntilChanged, map, shareReplay } from 'rxjs/operators';
import { AuthStore, CaseType, HealthDepartmentDto } from '@qro/auth/api';
import * as moment from 'moment';
import { Moment } from 'moment';

@Injectable({
  providedIn: 'root',
})
export class HealthDepartmentService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string, private authStore: AuthStore) {}

  resolveAnomalies(link: Link, comment: string) {
    return this.httpClient.put(link.href, { comment }).pipe(shareReplay());
  }

  createCase(caseDetail: CaseDto, type: CaseType): Observable<CaseDto> {
    return this.httpClient.post<CaseDto>(`${this.apiUrl}/hd/cases?type=${type}`, caseDetail).pipe(shareReplay());
  }

  updateCase(caseDetail: CaseDto): Observable<CaseDto> {
    return this.httpClient.put<CaseDto>(`${this.apiUrl}/hd/cases/${caseDetail.caseId}`, caseDetail).pipe(shareReplay());
  }

  addComment(caseId: string, comment: string): Observable<any> {
    return this.httpClient.post(`${this.apiUrl}/hd/cases/${caseId}/comments`, { comment }).pipe(shareReplay());
  }

  getCase(caseId: string): Observable<CaseDto> {
    return this.httpClient.get<CaseDto>(`${this.apiUrl}/hd/cases/${caseId}`).pipe(shareReplay());
  }

  getCaseActions(caseId: string): Observable<CaseActionDto> {
    return this.httpClient.get<CaseActionDto>(`${this.apiUrl}/hd/actions/${caseId}`).pipe(shareReplay());
  }

  getCsvDataByIdList(ids: string[]): Observable<any> {
    const options: Object = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      responseType: 'text/csv',
      observe: 'response',
    };
    return this.httpClient.post<string>(`${this.apiUrl}/hd/export/sormas/by-ids`, ids, options);
  }

  getQuarantineCsvData(caseType: CaseType, start: Moment, end: Moment): Observable<any> {
    const dto = {};

    if (caseType) {
      dto['type'] = caseType;
    }
    if (start && end) {
      dto['from'] = start.format('YYYY-MM-DD');
      dto['to'] = end.format('YYYY-MM-DD');
    }

    const options: Object = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      responseType: 'text/csv',
      observe: 'response',
    };

    return this.httpClient.post<string>(`${this.apiUrl}/hd/export/quarantines`, dto, options).pipe(shareReplay());
  }

  addEvent(caseId: string, event: any): Observable<any> {
    return this.httpClient.post(`${this.apiUrl}/hd/cases/${caseId}/occasions`, event).pipe(shareReplay());
  }

  getEvents(): Observable<any> {
    return this.httpClient.get(`${this.apiUrl}/hd/occasions`).pipe(shareReplay());
  }

  public get healthDepartment$(): Observable<HealthDepartmentDto> {
    return this.authStore.user$.pipe(
      distinctUntilChanged(),
      map((user) => user?.healthDepartment)
    );
  }
}
