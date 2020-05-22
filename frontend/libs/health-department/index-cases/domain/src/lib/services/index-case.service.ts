import { share, map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { API_URL } from '@quarano-frontend/shared/util';
import { Injectable, Inject } from '@angular/core';
import { ActionListItemDto, CaseListItemDto, ClientType } from '@quarano-frontend/health-department/domain';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class IndexCaseService {


  constructor(
    private httpClient: HttpClient,
    @Inject(API_URL) private apiUrl: string
  ) { }

  getCaseList(): Observable<CaseListItemDto[]> {
    return this.httpClient.get<any>(`${this.apiUrl}/api/hd/cases`)
      .pipe(
        share(),
        map(result => {
          if (result?._embedded?.cases) {
            return result._embedded.cases
              .filter((r: any) => r.caseType === ClientType.Index)
              .map((item: any) => this.mapCaseListItem(item));
          } else { return []; }
        }));
  }

  getActionList(): Observable<ActionListItemDto[]> {
    return this.httpClient.get<any[]>(`${this.apiUrl}/api/hd/actions`)
      .pipe(
        share(),
        map(result => result
          .filter(r => r.caseType === ClientType.Index)
          .map(item => this.mapActionListItem(item))));
  }

  private mapCaseListItem(item: any): CaseListItemDto {
    return {
      dateOfBirth: item.dateOfBirth ? new Date(item.dateOfBirth) : null,
      status: item.status,
      email: item.email,
      phone: item.primaryPhoneNumber,
      firstName: item.firstName,
      lastName: item.lastName,
      medicalStaff: item.medicalStaff,
      enrollmentCompleted: item.enrollmentCompleted,
      quarantineEnd: item.quarantine?.to ? new Date(item.quarantine.to) : null,
      quarantineStart: item.quarantine?.from ? new Date(item.quarantine.from) : null,
      caseType: item.caseType,
      zipCode: item.zipCode,
      caseId: item.caseId,
      caseTypeLabel: item.caseTypeLabel,
      createdAt: item.createdAt ? new Date(item.createdAt) : null,
      extReferenceNumber: item.extReferenceNumber
    };
  }

  private mapActionListItem(item: any): ActionListItemDto {
    return {
      dateOfBirth: item.dateOfBirth ? new Date(item.dateOfBirth) : null,
      caseId: item.caseId,
      caseType: item.caseType,
      name: item.name,
      priority: item.priority,
      firstName: item.firstName,
      lastName: item.lastName,
      phone: item.primaryPhoneNumber,
      email: item.email,
      quarantineEnd: item.quarantine?.to ? new Date(item.quarantine.to) : null,
      quarantineStart: item.quarantine?.from ? new Date(item.quarantine.from) : null,
      _links: item._links,
      alerts: item.healthSummary.concat(item.processSummary),
      status: item.status,
      caseTypeLabel: item.caseTypeLabel,
      createdAt: item.createdAt ? new Date(item.createdAt) : null,
      extReferenceNumber: item.extReferenceNumber
    };
  }
}
