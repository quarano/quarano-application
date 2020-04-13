import { environment } from './../../environments/environment';
import { SymptomDto } from './../models/symptom';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { DiaryEntryDto, DiaryEntryModifyDto } from '../models/diary-entry';
import { groupBy } from '../utils/groupBy';
import { ContactPersonDto } from '../models/contact-person';
import { TenantClientDto } from '../models/tenant-client';
import { ClientDto } from '../models/client';
import { HealthDepartmentDto } from '../models/healthDepartment';
import { UserDto } from '../models/user';

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

  getContactPerson(id: number): Observable<ContactPersonDto> {
    return this.httpClient.get<ContactPersonDto>(`${this.baseUrl}/api/contacts/${id}`).pipe(share());
  }

  getDiaryEntry(id: number): Observable<DiaryEntryDto> {
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

  getGroupedDiaryEntries(): Observable<Map<string, DiaryEntryDto[]>> {
    return this.getDiaryEntries()
      .pipe(
        map(entries => groupBy<DiaryEntryDto>(entries, e => e.date.toLocaleDateString())),
      );
  }

  getDiaryEntries(): Observable<DiaryEntryDto[]> {
    return this.httpClient.get<DiaryEntryDto[]>(`${this.baseUrl}/api/diary`)
      .pipe(
        share(),
        map(entries => {
          entries.forEach(e => e.date = this.getDate(e.date));
          return entries;
        }));
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

  registerClient(client: ClientDto): Observable<string> {
    return this.httpClient.post(`${this.baseUrl}/client/register`, client, { responseType: 'text' });
  }

  createContactPerson(contactPerson: ContactPersonDto): Observable<ContactPersonDto> {
    return this.httpClient.post<ContactPersonDto>(`${this.baseUrl}/api/contacts`, contactPerson);
  }

  modifyContactPerson(contactPerson: ContactPersonDto) {
    return this.httpClient.put(`${this.baseUrl}/api/contacts/${contactPerson.id}`, contactPerson);
  }

  getReport(healthDepartmentId: string): Observable<Array<TenantClientDto>> {
    return this.httpClient.get<Array<TenantClientDto>>(`${this.baseUrl}/report/${healthDepartmentId}`);
  }

  getHealthDepartment(healthDepartmentId: string): Observable<HealthDepartmentDto> {
    return this.httpClient.get<HealthDepartmentDto>(`${this.baseUrl}/healthdepartments/${healthDepartmentId}`);
  }

  login(username: string, password: string): Observable<{ token: string }> {
    return this.httpClient.post<{ token: string }>(`${this.baseUrl}/login`, { username, password });
  }

  getMe(): Observable<UserDto> {
    return this.httpClient.get<UserDto>(`${this.baseUrl}/user/me`);
  }

}
