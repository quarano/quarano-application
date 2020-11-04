import { ClientStore } from './../store/client-store.service';
import { API_URL } from '@qro/shared/util-data-access';
import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { shareReplay, tap } from 'rxjs/operators';
import { ClientDto } from '@qro/auth/api';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  constructor(
    private httpClient: HttpClient,
    @Inject(API_URL) private apiUrl: string,
    private clientStore: ClientStore
  ) {}

  getPersonalDetails(): Observable<ClientDto> {
    return this.httpClient.get<ClientDto>(`${this.apiUrl}/enrollment/details`).pipe(shareReplay());
  }

  updatePersonalDetails(client: ClientDto, confirmedZipCode: boolean = false): Observable<any> {
    let url = `${this.apiUrl}/enrollment/details`;
    if (confirmedZipCode) {
      url += '?confirmed=true';
    }
    return this.httpClient.put(url, client).pipe(
      shareReplay(),
      tap((_) => this.clientStore.loadEnrollmentStatus())
    );
  }
}
