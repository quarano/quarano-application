import { CaseSearchItem } from './../model/case-search-item';
import { share, map, shareReplay } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { API_URL } from '@qro/shared/util-data-access';
import { Injectable, Inject } from '@angular/core';
import { Observable } from 'rxjs';
import { CaseType } from '@qro/auth/api';
import { ActionListItemDto } from '../model/action-list-item';

@Injectable({
  providedIn: 'root',
})
export class IndexCaseService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  searchCases(searchTerm: string): Observable<CaseSearchItem[]> {
    return this.httpClient.get<any>(`${this.apiUrl}/api/hd/cases?type=index&q=${searchTerm}&projection=select`).pipe(
      shareReplay(),
      map((res) => res?._embedded?.cases)
    );
  }

  getActionList(): Observable<ActionListItemDto[]> {
    return this.httpClient.get<any>(`${this.apiUrl}/api/hd/actions`).pipe(
      shareReplay(),
      map((result) => {
        if (result?._embedded?.actions) {
          return result._embedded.actions
            .filter((r) => r.caseType === CaseType.Index)
            .map((item) => this.mapActionListItem(item));
        } else {
          return [];
        }
      })
    );
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
      extReferenceNumber: item.extReferenceNumber,
      originCases: item._embedded?.originCases || [],
    };
  }
}
