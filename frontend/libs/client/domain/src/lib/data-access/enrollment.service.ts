import { ClientStore } from './../store/client-store.service';
import { QuestionnaireDto } from '@qro/shared/util-data-access';
import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { map, shareReplay, tap } from 'rxjs/operators';
import { API_URL } from '@qro/shared/util-data-access';
import { RegisterDto } from '../model/register';
import { DateFunctions } from '@qro/shared/util-date';
import { EncounterEntry, EncountersDto, EncounterDto, EncounterCreateDto } from '../model/encounter';

@Injectable({
  providedIn: 'root',
})
export class EnrollmentService {
  private baseUrl = `${this.apiUrl}/api`;

  constructor(
    private httpClient: HttpClient,
    @Inject(API_URL) private apiUrl: string,
    private clientStore: ClientStore
  ) {}

  getQuestionnaire(): Observable<QuestionnaireDto> {
    return this.httpClient.get<QuestionnaireDto>(`${this.baseUrl}/enrollment/questionnaire`).pipe(shareReplay());
  }

  updateQuestionnaire(questionnaire: QuestionnaireDto): Observable<any> {
    return this.httpClient.put(`${this.baseUrl}/enrollment/questionnaire`, questionnaire).pipe(
      shareReplay(),
      tap((_) => this.clientStore.loadEnrollmentStatus())
    );
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

  registerClient(registerClient: RegisterDto): Observable<any> {
    return this.httpClient
      .post(`${this.baseUrl}/registration`, registerClient, { observe: 'response' })
      .pipe(shareReplay());
  }
}
