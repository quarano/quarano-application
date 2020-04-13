import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { QuestionnaireDto } from '../models/first-query';
import { ClientDto } from '../models/client';
import { tap, share, map } from 'rxjs/operators';
import { ClientStatusDto } from '../models/client-status';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {
  private baseUrl = `${environment.api.baseUrl}/api/enrollment`;

  constructor(private httpClient: HttpClient) { }


  getFirstQuery(): Observable<QuestionnaireDto> {
    return this.httpClient.get<QuestionnaireDto>(`${this.baseUrl}/questionnaire`)
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
    return this.httpClient.put(`${this.baseUrl}/questionnaire`, firstQuery);
  }

  getPersonalDetails(): Observable<ClientDto> {
    return this.httpClient.get<ClientDto>(`${this.baseUrl}/details`)
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
    return this.httpClient.put(`${this.baseUrl}/details`, client);
  }

  getEnrollmentStatus(): Observable<ClientStatusDto> {
    return this.httpClient.get<ClientStatusDto>(`${this.baseUrl}`)
      .pipe(share());
  }

}
