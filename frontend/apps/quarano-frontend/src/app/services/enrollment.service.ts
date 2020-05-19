import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {BehaviorSubject, forkJoin, Observable} from 'rxjs';
import {map, share, switchMap, tap} from 'rxjs/operators';
import '../utils/date-extensions';
import {environment} from '../../environments/environment';
import {EnrollmentStatusDto} from '../models/enrollment-status';
import {QuestionnaireDto} from '../models/first-query';
import {ClientDto} from '../models/client';
import {EncounterCreateDto, EncounterDto, EncounterEntry} from '../models/encounter';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {
  private baseUrl = `${environment.api.baseUrl}/api`;
  private enrollmentSubject$ = new BehaviorSubject<EnrollmentStatusDto>(null);

  constructor(
    private httpClient: HttpClient) {
  }

  getQuestionnaire(): Observable<QuestionnaireDto> {
    return this.httpClient.get<QuestionnaireDto>(`${this.baseUrl}/enrollment/questionnaire`).pipe(share());
  }

  updateQuestionnaire(questionnaire: QuestionnaireDto): Observable<any> {
    return this.httpClient.put(`${this.baseUrl}/enrollment/questionnaire`, questionnaire)
      .pipe(switchMap(_ => this.loadEnrollmentStatus()));
  }

  getPersonalDetails(): Observable<ClientDto> {
    return this.httpClient.get<ClientDto>(`${this.baseUrl}/enrollment/details`).pipe(share());
  }

  updatePersonalDetails(client: ClientDto): Observable<any> {
    return this.httpClient.put(`${this.baseUrl}/enrollment/details`, client)
      .pipe(switchMap(_ => this.loadEnrollmentStatus()));
  }

  loadEnrollmentStatus(): Observable<EnrollmentStatusDto> {
    return this.httpClient.get<EnrollmentStatusDto>(`${this.baseUrl}/enrollment`).pipe(share()).pipe(
      tap((data) => this.enrollmentSubject$.next(data)),
      switchMap(() => this.enrollmentSubject$)
    );
  }

  getEnrollmentStatus(): Observable<EnrollmentStatusDto> {
    return this.enrollmentSubject$;
  }

  getEncounters(): Observable<EncounterEntry[]> {
    return this.httpClient.get<EncounterDto[]>(`${this.baseUrl}/encounters`)
      .pipe(share(), map(encounters => encounters.map(e => this.mapEncounterToEncounterEntry(e))));
  }

  private mapEncounterToEncounterEntry(dto: EncounterDto): EncounterEntry {
    return {encounter: dto, date: dto.date, contactPersonId: dto._links.contact.href.split('/').slice(-1)[0]};
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
      contactIds.map(id => this.createEncounter({contact: id, date: dateString}))
    );
  }

  deleteEncounter(encounter: EncounterDto) {
    return this.httpClient.delete(encounter._links.self.href);
  }

  completeEnrollment(withoutEncounters: boolean): Observable<any> {
    return this.httpClient
      .post(`${this.baseUrl}/enrollment/completion?withoutEncounters=${withoutEncounters}`, {})
      .pipe(switchMap(() => this.loadEnrollmentStatus()));
  }

  reopenEnrollment(): Observable<EnrollmentStatusDto> {
    return this.httpClient.delete(`${this.baseUrl}/enrollment/completion`)
      .pipe(switchMap(() => this.getEnrollmentStatus()));
  }
}
