import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { FirstQuery } from '../models/first-query';
import { Client } from '../models/client';
import { tap, share, map } from 'rxjs/operators';
import { ClientStatus } from '../models/client-status';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {
  private baseUrl = `${environment.api.baseUrl}/api/enrollment`;

  constructor(private httpClient: HttpClient) { }


  getFirstQuery(): Observable<FirstQuery> {
    return this.httpClient.get<FirstQuery>(`${this.baseUrl}/questionaire`)
      .pipe(
        share(),
        map(result => {
          if (result.dayOfFirstSymptoms) {
            result.dayOfFirstSymptoms = new Date(result.dayOfFirstSymptoms);
          }
          return result;
        }));
  }

  updateFirstQuery(firstQuery: FirstQuery) {
    return this.httpClient.put(`${this.baseUrl}/questionaire`, firstQuery);
  }

  getPersonalDetails(): Observable<Client> {
    return this.httpClient.get<Client>(`${this.baseUrl}/details`)
      .pipe(
        share(),
        map(result => {
          if (result.dateOfBirth) {
            result.dateOfBirth = new Date(result.dateOfBirth);
          }
          return result;
        }));
  }

  updatePersonalDetails(client: Client) {
    return this.httpClient.put(`${this.baseUrl}/details`, client);
  }

  getEnrollmentStatus(): Observable<ClientStatus> {
    return this.httpClient.get<ClientStatus>(`${this.baseUrl}`)
      .pipe(share());
  }

}
