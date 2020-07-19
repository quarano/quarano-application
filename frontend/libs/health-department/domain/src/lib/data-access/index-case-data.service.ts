import { CaseDto } from './../model/case';
import { CASE_FEATURE_KEY } from './index-case-entity.service';
import { Injectable, Inject } from '@angular/core';
import { DefaultDataService, HttpUrlGenerator, QueryParams } from '@ngrx/data';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { shareReplay, map } from 'rxjs/operators';
import { API_URL } from '@qro/shared/util-data-access';
import { CaseDetailDto } from '../model/case-detail';

@Injectable()
export class IndexCaseDataService extends DefaultDataService<any> {
  constructor(http: HttpClient, httpUrlGenerator: HttpUrlGenerator, @Inject(API_URL) private apiUrl: string) {
    super(CASE_FEATURE_KEY, http, httpUrlGenerator);
  }

  getAll(): Observable<CaseDto[]> {
    return this.http.get<any>(`${this.apiUrl}/api/hd/cases`).pipe(
      shareReplay(),
      map((result) => {
        if (result?._embedded?.cases) {
          return result._embedded.cases.map((item: any) => this.mapCaseListItem(item));
        } else {
          return [];
        }
      })
    );
  }

  getWithQuery(queryParams: string | QueryParams): Observable<CaseDto[]> {
    const qParams = typeof queryParams === 'string' ? { fromString: queryParams } : { fromObject: queryParams };
    const params = new HttpParams(qParams);
    return this.http
      .get<any>(`${this.apiUrl}/api/hd/cases`, { params })
      .pipe(
        shareReplay(),
        map((result) => {
          if (result?._embedded?.cases) {
            return result._embedded.cases.map((item: any) => this.mapCaseListItem(item));
          } else {
            return [];
          }
        })
      );
  }

  getById(id: any): Observable<CaseDetailDto> {
    return this.http.get<CaseDetailDto>(`${this.apiUrl}/api/hd/cases/${id}`);
  }

  private mapCaseListItem(item: any): CaseDto {
    return {
      dateOfBirth: item.dateOfBirth ? new Date(item.dateOfBirth) : null,
      status: item.status,
      email: item.email,
      phone: item.primaryPhoneNumber,
      firstName: item.firstName,
      lastName: item.lastName,
      medicalStaff: item.medicalStaff,
      enrollmentCompleted: item.enrollmentCompleted,
      quarantineEndDate: item.quarantine?.to ? new Date(item.quarantine.to) : null,
      quarantineStartDate: item.quarantine?.from ? new Date(item.quarantine.from) : null,
      caseType: item.caseType,
      zipCode: item.zipCode,
      caseId: item.caseId,
      caseTypeLabel: item.caseTypeLabel,
      createdAt: item.createdAt ? new Date(item.createdAt) : null,
      extReferenceNumber: item.extReferenceNumber,
    };
  }
}
