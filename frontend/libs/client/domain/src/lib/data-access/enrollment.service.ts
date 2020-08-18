import { QuestionnaireDto } from '@qro/shared/util-data-access';
import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { BehaviorSubject, forkJoin, Observable } from 'rxjs';
import { map, share, switchMap, tap, shareReplay } from 'rxjs/operators';
import { API_URL } from '@qro/shared/util-data-access';
import { EnrollmentStatusDto } from '../model/enrollment-status';
import { RegisterDto } from '../model/register';
import { DateFunctions } from '@qro/shared/util-date';
import { EncounterEntry, EncountersDto, EncounterDto, EncounterCreateDto } from '../model/encounter';

@Injectable({
  providedIn: 'root',
})
export class EnrollmentService {
  private baseUrl = `${this.apiUrl}/api`;
  private enrollmentSubject$$ = new BehaviorSubject<EnrollmentStatusDto>(null);

  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  getQuestionnaire(): Observable<QuestionnaireDto> {
    return this.httpClient.get<QuestionnaireDto>(`${this.baseUrl}/enrollment/questionnaire`).pipe(shareReplay());
  }

  updateQuestionnaire(questionnaire: QuestionnaireDto): Observable<any> {
    return this.httpClient.put(`${this.baseUrl}/enrollment/questionnaire`, questionnaire).pipe(
      shareReplay(),
      switchMap((_) => this.loadEnrollmentStatus())
    );
  }

  loadEnrollmentStatus(): Observable<EnrollmentStatusDto> {
    return this.httpClient
      .get<EnrollmentStatusDto>(`${this.baseUrl}/enrollment`)
      .pipe(shareReplay())
      .pipe(
        tap((data) => this.enrollmentSubject$$.next(data)),
        switchMap(() => this.enrollmentSubject$$)
      );
  }

  getEnrollmentStatus(): Observable<EnrollmentStatusDto> {
    if (!this.enrollmentSubject$$.value) {
      return this.loadEnrollmentStatus();
    }
    return this.enrollmentSubject$$;
  }

  getEncounters(): Observable<EncounterEntry[]> {
    return this.httpClient.get<EncountersDto>(`${this.baseUrl}/encounters`).pipe(
      shareReplay(),
      map((encounters) => encounters?._embedded?.encounters.map((e) => this.mapEncounterToEncounterEntry(e)) || [])
    );
  }

  private mapEncounterToEncounterEntry(dto: EncounterDto): EncounterEntry {
    return { encounter: dto, date: dto.date, contactPersonId: dto._links.contact.href.split('/').slice(-1)[0] };
  }

  createEncounter(createDto: EncounterCreateDto): Observable<EncounterEntry> {
    return this.httpClient.post<EncounterDto>(`${this.baseUrl}/encounters`, createDto).pipe(
      shareReplay(),
      map((encounter) => {
        return this.mapEncounterToEncounterEntry(encounter);
      })
    );
  }

  createEncounters(date: Date, contactIds: string[]): Observable<EncounterEntry[]> {
    const dateString = DateFunctions.getDateWithoutTime(date);
    return forkJoin(contactIds.map((id) => this.createEncounter({ contact: id, date: dateString })));
  }

  deleteEncounter(encounter: EncounterDto) {
    return this.httpClient.delete(encounter._links.self.href).pipe(shareReplay());
  }

  completeEnrollment(withoutEncounters: boolean): Observable<any> {
    return this.httpClient
      .post(`${this.baseUrl}/enrollment/completion?withoutEncounters=${withoutEncounters}`, {})
      .pipe(
        shareReplay(),
        switchMap(() => this.loadEnrollmentStatus())
      );
  }

  reopenEnrollment(): Observable<EnrollmentStatusDto> {
    return this.httpClient.delete(`${this.baseUrl}/enrollment/completion`).pipe(
      shareReplay(),
      switchMap(() => this.getEnrollmentStatus())
    );
  }

  registerClient(registerClient: RegisterDto): Observable<any> {
    return this.httpClient
      .post(`${this.baseUrl}/registration`, registerClient, { observe: 'response' })
      .pipe(shareReplay());
  }
}
