import { API_URL } from '@qro/shared/util-data-access';
import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { shareReplay } from 'rxjs/operators';
import { ClientDto } from '@qro/auth/api';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  getPersonalDetails(): Observable<ClientDto> {
    return this.httpClient.get<ClientDto>(`${this.apiUrl}/details`).pipe(shareReplay());
  }

  updatePersonalDetails(client: ClientDto): Observable<any> {
    return this.httpClient.put(`${this.apiUrl}/details`, client).pipe(shareReplay());
  }
}
