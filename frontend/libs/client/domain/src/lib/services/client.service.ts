import { EnrollmentService } from '@qro/client/enrollment/api';
import { API_URL } from '@qro/shared/util';
import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClientDto } from '../models/client';
import { share, switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  constructor(
    private httpClient: HttpClient,
    @Inject(API_URL) private apiUrl: string,
    private enrollmentService: EnrollmentService
  ) {}

  getPersonalDetails(): Observable<ClientDto> {
    return this.httpClient.get<ClientDto>(`${this.apiUrl}/enrollment/details`).pipe(share());
  }

  updatePersonalDetails(client: ClientDto): Observable<any> {
    return this.httpClient
      .put(`${this.apiUrl}/enrollment/details`, client)
      .pipe(switchMap((_) => this.enrollmentService.loadEnrollmentStatus()));
  }
}
