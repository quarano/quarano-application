import { map, shareReplay } from 'rxjs/operators';
import { API_URL } from '@qro/shared/util-data-access';
import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CaseType } from '@qro/auth/api';
import { ActionListItemDto } from '../model/action-list-item';

@Injectable({
  providedIn: 'root',
})
export class ContactCaseService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  getActionList(): Observable<ActionListItemDto[]> {
    return this.httpClient.get<any>(`${this.apiUrl}/hd/actions`).pipe(
      shareReplay(),
      map((result) => {
        if (result?._embedded?.actions) {
          return result._embedded.actions
            .filter((r) => r.caseType === CaseType.Contact)
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
      originCases: item._embedded?.originCases || [],
      alerts: item.healthSummary.concat(item.processSummary),
      status: item.status,
      caseTypeLabel: item.caseTypeLabel,
      createdAt: item.createdAt ? new Date(item.createdAt) : null,
      extReferenceNumber: item.extReferenceNumber,
    };
  }
}
