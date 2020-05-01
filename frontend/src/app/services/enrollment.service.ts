import { EncounterEntry } from '@models/encounter';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environment/environment';
import { Observable, forkJoin } from 'rxjs';
import { QuestionnaireDto } from '@models/first-query';
import { ClientDto } from '@models/client';
import { share, map, switchMap } from 'rxjs/operators';
import { EnrollmentStatusDto } from '@models/enrollment-status';
import { EncounterDto, EncounterCreateDto } from '@models/encounter';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {
  private baseUrl = `${environment.api.baseUrl}/api`;

  constructor(
    private httpClient: HttpClient) { }

  getQuestionnaire(): Observable<QuestionnaireDto> {
    return this.httpClient.get<QuestionnaireDto>(`${this.baseUrl}/enrollment/questionnaire`)
      .pipe(
        share(),
        map(result => {
          if (result.dayOfFirstSymptoms) {
            result.dayOfFirstSymptoms = new Date(result.dayOfFirstSymptoms);
          }
          return result;
        }));
  }

  updateQuestionnaire(questionnaire: QuestionnaireDto) {
    return this.httpClient.put(`${this.baseUrl}/enrollment/questionnaire`, questionnaire);
  }

  getPersonalDetails(): Observable<ClientDto> {
    return this.httpClient.get<ClientDto>(`${this.baseUrl}/enrollment/details`)
      .pipe(
        share(),
        map(result => {
          if (result.dateOfBirth) {
            result.dateOfBirth = new Date(result.dateOfBirth);
          }
          return result;
        }));
  }

  updatePersonalDetails(client: ClientDto): Observable<EnrollmentStatusDto> {
    return this.httpClient.put(`${this.baseUrl}/enrollment/details`, client)
      .pipe(switchMap(_ => this.getEnrollmentStatus()));
  }

  getEnrollmentStatus(): Observable<EnrollmentStatusDto> {
    return this.httpClient.get<EnrollmentStatusDto>(`${this.baseUrl}/enrollment`)
      .pipe(
        share());
  }

  getEncounters(): Observable<EncounterEntry[]> {
    return this.httpClient.get<EncounterDto[]>(`${this.baseUrl}/encounters`)
      .pipe(share(), map(encounters => encounters.map(e => this.mapEncounterToEncounterEntry(e))));
  }

  private mapEncounterToEncounterEntry(dto: EncounterDto): EncounterEntry {
    return { encounter: dto, date: dto.date, contactPersonId: dto._links.contact.href.split('/').slice(-1)[0] };
  }

  createEncounter(createDto: EncounterCreateDto): Observable<EncounterEntry> {
    return this.httpClient.post<EncounterDto>(`${this.baseUrl}/encounters`, createDto)
      .pipe(map(encounter => {
        return this.mapEncounterToEncounterEntry(encounter);
      }));
  }

  createEncounters(date: Date, contactIds: string[]): Observable<EncounterEntry[]> {
    const dateString = date.getDateWithoutTime();
    return forkJoin(
      contactIds.map(id => this.createEncounter({ contact: id, date: dateString }))
    );
  }

  deleteEncounter(encounter: EncounterDto) {
    return this.httpClient.delete(encounter._links.self.href);
  }

  completeEnrollment(withoutEncounters: boolean): Observable<EnrollmentStatusDto> {
    return this.httpClient
      .post(`${this.baseUrl}/enrollment/completion?withoutEncounters=${withoutEncounters}`, {})
      .pipe(switchMap(_ => this.getEnrollmentStatus()));
  }

  reopenEnrollment(): Observable<EnrollmentStatusDto> {
    return this.httpClient.delete(`${this.baseUrl}/enrollment/completion`)
      .pipe(switchMap(_ => this.getEnrollmentStatus()));
  }
}
