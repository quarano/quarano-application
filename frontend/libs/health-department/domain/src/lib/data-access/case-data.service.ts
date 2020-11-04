import { CaseDto } from '../model/case';
import { CASE_FEATURE_KEY } from './case-entity.service';
import { Injectable, Inject } from '@angular/core';
import { DefaultDataService, HttpUrlGenerator, QueryParams } from '@ngrx/data';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { API_URL } from '@qro/shared/util-data-access';
import { Update } from '@ngrx/entity';

@Injectable()
export class CaseDataService extends DefaultDataService<CaseDto> {
  constructor(http: HttpClient, httpUrlGenerator: HttpUrlGenerator, @Inject(API_URL) private apiUrl: string) {
    super(CASE_FEATURE_KEY, http, httpUrlGenerator, { root: `${apiUrl}/hd` });
  }

  getAll(): Observable<CaseDto[]> {
    return this.execute('GET', this.entitiesUrl).pipe(
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
    return this.execute('GET', this.entitiesUrl, undefined, { params }).pipe(
      map((result) => {
        if (result?._embedded?.cases) {
          return result._embedded.cases.map((item: any) => this.mapCaseListItem(item));
        } else {
          return [];
        }
      })
    );
  }

  getById(key: number | string): Observable<CaseDto> {
    let err: Error | undefined;
    if (key == null) {
      err = new Error(`No "${this.entityName}" key to get`);
    }
    return this.execute('GET', this.entitiesUrl + key, err);
  }

  update(update: Update<CaseDto>): Observable<CaseDto> {
    const id = update && update.id;
    const updateOrError = id == null ? new Error(`No "${this.entityName}" update data or id`) : update.changes;
    return this.execute('PUT', this.entitiesUrl + id, updateOrError);
  }

  add(entity: CaseDto): Observable<CaseDto> {
    const qParams = { fromObject: { type: entity.caseType } };
    const params = new HttpParams(qParams);
    const entityOrError = entity || new Error(`No "${this.entityName}" entity to add`);
    return this.execute('POST', this.entitiesUrl, entityOrError, { params });
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
      _embedded: item._embedded,
    };
  }
}
