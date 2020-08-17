import { CaseDto } from './../model/case';
import { API_URL } from '@qro/shared/util-data-access';
import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Link } from '@qro/shared/util-data-access';
import { Observable } from 'rxjs';
import { CaseActionDto } from '../model/case-action';
import { share, distinctUntilChanged, map, shareReplay } from 'rxjs/operators';
import { UserService, HealthDepartmentDto, CaseType } from '@qro/auth/api';

@Injectable({
  providedIn: 'root',
})
export class HealthDepartmentService {
  constructor(
    private httpClient: HttpClient,
    @Inject(API_URL) private apiUrl: string,
    private userService: UserService
  ) {}

  resolveAnomalies(link: Link, comment: string) {
    return this.httpClient.put(link.href, { comment }).pipe(shareReplay());
  }

  createCase(caseDetail: CaseDto, type: CaseType): Observable<CaseDto> {
    return this.httpClient.post<CaseDto>(`${this.apiUrl}/api/hd/cases?type=${type}`, caseDetail).pipe(shareReplay());
  }

  updateCase(caseDetail: CaseDto): Observable<CaseDto> {
    return this.httpClient
      .put<CaseDto>(`${this.apiUrl}/api/hd/cases/${caseDetail.caseId}`, caseDetail)
      .pipe(shareReplay());
  }

  addComment(caseId: string, comment: string): Observable<any> {
    return this.httpClient.post(`${this.apiUrl}/api/hd/cases/${caseId}/comments`, { comment }).pipe(shareReplay());
  }

  getCase(caseId: string): Observable<CaseDto> {
    return this.httpClient.get<CaseDto>(`${this.apiUrl}/api/hd/cases/${caseId}`).pipe(shareReplay());
  }

  getCaseActions(caseId: string): Observable<CaseActionDto> {
    return this.httpClient.get<CaseActionDto>(`${this.apiUrl}/api/hd/actions/${caseId}`).pipe(shareReplay());
  }

  public get healthDepartment$(): Observable<HealthDepartmentDto> {
    return this.userService.user$.pipe(
      distinctUntilChanged(),
      map((user) => user?.healthDepartment)
    );
  }
}
