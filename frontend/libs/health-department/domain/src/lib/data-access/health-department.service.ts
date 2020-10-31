import { CaseDto } from './../model/case';
import { API_URL, Link } from '@qro/shared/util-data-access';
import { Inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CaseActionDto } from '../model/case-action';
import { distinctUntilChanged, map, shareReplay } from 'rxjs/operators';
import { AuthStore, CaseType, HealthDepartmentDto } from '@qro/auth/api';
import { TrackedCaseDiaryEntryDto } from '../..';

@Injectable({
  providedIn: 'root',
})
export class HealthDepartmentService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string, private authStore: AuthStore) {}

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

  getCaseDiaryEntries(caseId: string): Observable<TrackedCaseDiaryEntryDto[]> {
    return this.httpClient.get<any>(`${this.apiUrl}/api/hd/cases/${caseId}/diary`).pipe(
      map((result) => result?._embedded?.trackedCaseDiaryEntrySummaryList),
      map((trackedCasesList) => this.mapToTrackedCaseDiaryEntryDtoList(trackedCasesList)),
      shareReplay()
    );
  }

  private mapToTrackedCaseDiaryEntryDtoList(diaryEntriesList: any): TrackedCaseDiaryEntryDto[] {
    return diaryEntriesList.map((entry) => {
      return {
        bodyTemperature: entry.bodyTemperature,
        timeOfDay: entry.slot.timeOfDay,
        date: entry.slot.date,
        symptoms: entry.symptoms.map((symtom) => symtom.name),
        contacts: entry.contacts.map((contact) => contact.firstName + ' ' + contact.lastName),
      } as TrackedCaseDiaryEntryDto;
    });
  }

  getCaseActions(caseId: string): Observable<CaseActionDto> {
    return this.httpClient.get<CaseActionDto>(`${this.apiUrl}/api/hd/actions/${caseId}`).pipe(shareReplay());
  }

  public get healthDepartment$(): Observable<HealthDepartmentDto> {
    return this.authStore.user$.pipe(
      distinctUntilChanged(),
      map((user) => user?.healthDepartment)
    );
  }
}
