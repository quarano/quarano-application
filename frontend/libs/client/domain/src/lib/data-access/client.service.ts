import { API_URL } from '@qro/shared/util-data-access';
import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { switchMap, shareReplay } from 'rxjs/operators';
import { EnrollmentService } from './enrollment.service';
import { ClientDto } from '@qro/auth/api';

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
    return this.httpClient.get<ClientDto>(`${this.apiUrl}/api/enrollment/details`).pipe(shareReplay());
  }

  updatePersonalDetails(client: ClientDto): Observable<any> {
    return this.httpClient.put(`${this.apiUrl}/api/enrollment/details`, client).pipe(
      shareReplay(),
      switchMap((_) => this.enrollmentService.loadEnrollmentStatus())
    );
  }
}
