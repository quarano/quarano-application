import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { QuestionnaireDto } from '../models/first-query';
import { ClientDto } from '../models/client';
import { tap, share, map } from 'rxjs/operators';
import { ClientStatusDto } from '../models/client-status';
import { EncounterDto, EncounterCreateDto } from '../models/encounter';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {
  private baseUrl = `${environment.api.baseUrl}/api`;

  constructor(private httpClient: HttpClient) { }


  getFirstQuery(): Observable<QuestionnaireDto> {
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

  updateFirstQuery(firstQuery: QuestionnaireDto) {
    return this.httpClient.put(`${this.baseUrl}/enrollment/questionnaire`, firstQuery);
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

  updatePersonalDetails(client: ClientDto) {
    return this.httpClient.put(`${this.baseUrl}/enrollment/details`, client);
  }

  getEnrollmentStatus(): Observable<ClientStatusDto> {
    return this.httpClient.get<ClientStatusDto>(`${this.baseUrl}`)
      .pipe(share());
  }

  getEncounters(): Observable<EncounterDto[]> {
    return this.httpClient.get<EncounterDto[]>(`${this.baseUrl}/encounters`)
      .pipe(share(), map(encounters => {
        return encounters.map(encounter => {
          encounter.date = new Date(encounter.date);
          return encounter;
        });
      }));
  }

  createEncounter(createDto: EncounterCreateDto): Observable<EncounterDto> {
    return this.httpClient.post<EncounterDto>(`${this.baseUrl}/encounters`, createDto)
      .pipe(map(encounter => {
        encounter.date = new Date(encounter.date);
        return encounter;
      }));
  }

  completeEnrollment(withoutEncounters: boolean) {
    const params = new HttpParams();
    params.append('withoutEncounters', withoutEncounters.toString());
    return this.httpClient.post(`${this.baseUrl}/enrollment/completion`, { params });
  }

  reopenEnrollment() {
    return this.httpClient.delete(`${this.baseUrl}/enrollment/completion`);
  }
}
