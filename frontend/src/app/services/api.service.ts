import { UserDto } from '@models/user';
import { environment } from '@environment/environment';
import { SymptomDto } from '@models/symptom';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { DiaryEntryDto, DiaryEntryModifyDto, DiaryDto } from '@models/diary-entry';
import { groupBy } from '@utils/groupBy';
import { ContactPersonDto, ContactPersonModifyDto } from '@models/contact-person';
import { Register } from '@models/register';
import { ReportCaseDto } from '@models/report-case';
import { ActionListItemDto } from '@models/action';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = environment.api.baseUrl;

  constructor(protected httpClient: HttpClient) {
  }

  getSymptoms(): Observable<SymptomDto[]> {
    return this.httpClient.get<SymptomDto[]>(`${this.baseUrl}/api/symptoms`).pipe(share());
  }

  getContactPersons(): Observable<ContactPersonDto[]> {
    return this.httpClient.get<ContactPersonDto[]>(`${this.baseUrl}/api/contacts`).pipe(share());
  }

  getContactPerson(id: string): Observable<ContactPersonDto> {
    return this.httpClient.get<ContactPersonDto>(`${this.baseUrl}/api/contacts/${id}`).pipe(share());
  }

  getDiaryEntry(id: string): Observable<DiaryEntryDto> {
    return this.httpClient.get<DiaryEntryDto>(`${this.baseUrl}/api/diary/${id}`)
      .pipe(
        share(),
        map(entry => {
          entry.characteristicSymptoms = entry.symptoms.filter(s => s.characteristic);
          entry.nonCharacteristicSymptoms = entry.symptoms.filter(s => !s.characteristic);
          entry.date = this.getDate(entry.date);
          return entry;
        }),
      );
  }

  private getDate(date: Date): Date {
    return new Date(date + 'Z');
  }

  createDiaryEntry(diaryEntry: DiaryEntryModifyDto): Observable<DiaryEntryDto> {
    return this.httpClient.post<DiaryEntryDto>(`${this.baseUrl}/api/diary`, diaryEntry)
      .pipe(map(entry => {
        entry.date = this.getDate(entry.date);
        return entry;
      }));
  }

  modifyDiaryEntry(diaryEntry: DiaryEntryModifyDto) {
    return this.httpClient.put(`${this.baseUrl}/api/diary/${diaryEntry.id}`, diaryEntry);
  }

  registerClient(registerClient: Register): Observable<string> {
    return this.httpClient.post(`${this.baseUrl}/api/registration`, registerClient, { responseType: 'text' });
  }

  createContactPerson(contactPerson: ContactPersonModifyDto): Observable<ContactPersonDto> {
    return this.httpClient.post<ContactPersonDto>(`${this.baseUrl}/api/contacts`, contactPerson);
  }

  modifyContactPerson(contactPerson: ContactPersonModifyDto, id: string) {
    return this.httpClient.put(`${this.baseUrl}/api/contacts/${id}`, contactPerson);
  }

  login(username: string, password: string): Observable<{ token: string }> {
    return this.httpClient.post<{ token: string }>(`${this.baseUrl}/login`, { username, password });
  }

  getMe(): Observable<UserDto> {
    return this.httpClient.get<UserDto>(`${this.baseUrl}/api/user/me`);
  }

  checkClientCode(code: string): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/api/registration/checkcode/${code}`);
  }

  checkUsername(username: string): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/api/registration/checkusername/${username}`);
  }

  getCases(): Observable<Array<ReportCaseDto>> {
    return this.httpClient.get<Array<ReportCaseDto>>(`${this.baseUrl}/api/hd/cases`);
  }

  getDiary(): Observable<DiaryDto> {
    return this.httpClient.get<DiaryDto>(`${this.baseUrl}/api/diary`);
  }

  getAllActions(): Observable<ActionListItemDto[]> {
    return this.httpClient.get<any[]>(`${this.baseUrl}/api/hd/actions`)
      .pipe(map(result => result.map(item => this.mapActionListItem(item))));
  }

  private mapActionListItem(item: any): ActionListItemDto {
    return {
      dateOfBirth: new Date(item.dateOfBirth),
      caseId: item.caseId,
      caseType: item.caseType,
      name: item.name,
      priority: item.priority,
      firstName: item.firstName,
      lastName: item.lastName,
      phone: item.phone,
      email: item.email,
      quarantineEnd: new Date(item.quarantineEnd),
      quarantineStart: new Date(item.quarantineStart),
      _links: item._links,
      alerts: item.healthSummary.concat(item.processSummary)
    };
  }
}
