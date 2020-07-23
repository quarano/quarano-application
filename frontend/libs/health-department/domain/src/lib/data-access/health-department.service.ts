import { CaseDto } from './../model/case';
import { API_URL } from '@qro/shared/util-data-access';
import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Link } from '@qro/shared/util-data-access';
import { Observable } from 'rxjs';
import { CaseActionDto } from '../model/case-action';
import { share, distinctUntilChanged, map } from 'rxjs/operators';
import { UserService, HealthDepartmentDto } from '@qro/auth/api';

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
    return this.httpClient.put(link.href, { comment });
  }

  addComment(caseId: string, comment: string): Observable<CaseDto> {
    return this.httpClient.post<CaseDto>(`${this.apiUrl}/api/hd/cases/${caseId}/comments`, { comment });
  }

  getCaseActions(caseId: string): Observable<CaseActionDto> {
    return this.httpClient.get<CaseActionDto>(`${this.apiUrl}/api/hd/actions/${caseId}`).pipe(share());
  }

  public get healthDepartment$(): Observable<HealthDepartmentDto> {
    return this.userService.user$.pipe(
      distinctUntilChanged(),
      map((user) => user?.healthDepartment)
    );
  }
}
