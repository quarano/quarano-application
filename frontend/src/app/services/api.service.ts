import {environment} from './../../environments/environment';
import {SymptomDto} from './../models/symptom';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BackendClient} from '../models/backend-client';
import {map, share} from 'rxjs/operators';
import {DiaryEntryDto, DiaryEntryModifyDto} from '../models/diary-entry';
import {groupBy} from '../utils/groupBy';
import {FirstQuery} from '../models/first-query';
import {ContactPersonDto} from '../models/contact-person';
import {TenantClient} from '../models/tenant-client';
import {Client} from '../models/client';
import {HealthDepartmentDto} from '../models/healtDepartment';
import {User} from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = environment.api.baseUrl;

  constructor(protected httpClient: HttpClient) {
  }

  getSymptoms(): Observable<SymptomDto[]> {
    return this.httpClient.get<SymptomDto[]>(`${this.baseUrl}/symptoms`).pipe(share());
  }

  getContactPersons(): Observable<ContactPersonDto[]> {
    return this.httpClient.get<ContactPersonDto[]>(`${this.baseUrl}/contact/`).pipe(share());
  }

  getContactPerson(id: number): Observable<ContactPersonDto> {
    return this.httpClient.get<ContactPersonDto>(`${this.baseUrl}/contact/${id}`).pipe(share());
  }

  getDiaryEntry(id: number): Observable<DiaryEntryDto> {
    return this.httpClient.get<DiaryEntryDto>(`${this.baseUrl}/diaryentries/${id}`)
      .pipe(
        share(),
        map(entry => {
          entry.characteristicSymptoms = entry.symptoms.filter(s => s.characteristic);
          entry.nonCharacteristicSymptoms = entry.symptoms.filter(s => !s.characteristic);
          entry.dateTime = this.getDate(entry.dateTime);
          return entry;
        }),
      );
  }

  private getDate(date: Date): Date {
    return new Date(date + 'Z');
  }

  getGroupedDiaryEntries(): Observable<Map<string, DiaryEntryDto[]>> {
    return this.getDiaryEntries()
      .pipe(
        map(entries => groupBy<DiaryEntryDto>(entries, e => e.dateTime.toLocaleDateString())),
      );
  }

  getDiaryEntries(): Observable<DiaryEntryDto[]> {
    return this.httpClient.get<DiaryEntryDto[]>(`${this.baseUrl}/diaryentries`)
      .pipe(
        share(),
        map(entries => {
          entries.forEach(e => e.dateTime = this.getDate(e.dateTime));
          return entries;
        }));
  }

  createDiaryEntry(diaryEntry: DiaryEntryModifyDto): Observable<DiaryEntryDto> {
    return this.httpClient.post<DiaryEntryDto>(`${this.baseUrl}/diaryentries`, diaryEntry);
  }

  modifyDiaryEntry(diaryEntry: DiaryEntryModifyDto) {
    return this.httpClient.put(`${this.baseUrl}/diaryentries/${diaryEntry.id}`, diaryEntry);
  }

  registerClient(client: Client): Observable<string> {
    return this.httpClient.post(`${this.baseUrl}/client/register`, client, { responseType: 'text' });
  }

  getClientByCode(code: string): Observable<Client> {
    return this.httpClient.get<BackendClient>(`${this.baseUrl}/client/${code}`);
  }

  createFirstReport(firstReport: FirstQuery, clientCode: string): Observable<any> {
    return this.httpClient.post(`${this.baseUrl}/firstreport/${clientCode}`, firstReport);
  }

  createContactPerson(contactPerson: ContactPersonDto): Observable<ContactPersonDto> {
    return this.httpClient.post<ContactPersonDto>(`${this.baseUrl}/contact/`, contactPerson);
  }

  modifyContactPerson(contactPerson: ContactPersonDto) {
    return this.httpClient.put(`${this.baseUrl}/contact/${contactPerson.id}`, contactPerson);
  }

  getReport(healthDepartmentId: string): Observable<Array<TenantClient>> {
    return this.httpClient.get<Array<TenantClient>>(`${this.baseUrl}/report/${healthDepartmentId}`);
  }

  getHealthDepartment(healthDepartmentId: string): Observable<HealthDepartmentDto> {
    return this.httpClient.get<HealthDepartmentDto>(`${this.baseUrl}/healthdepartments/${healthDepartmentId}`);
  }

  login(username: string, password: string): Observable<{token: string}> {
    return this.httpClient.post<{token: string}>(`${this.baseUrl}/login`, {username, password});
  }

  getMe(): Observable<User> {
    return this.httpClient.get<User>(`${this.baseUrl}/user/me`);
  }
}
