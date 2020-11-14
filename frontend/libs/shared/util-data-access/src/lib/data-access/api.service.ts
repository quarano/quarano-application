import { shareReplay } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { HalResponse } from '../model/hal-response';
import { Observable, of } from 'rxjs';
import { DeleteLink } from '../model/general';
import { API_URL } from '../model/api-url';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  getApiCall<T>(halResponse: HalResponse, key: string): Observable<T> {
    if (halResponse._links?.hasOwnProperty(key)) {
      let url = halResponse._links[key].href;
      if (Array.isArray(halResponse._links[key])) {
        url = halResponse._links[key][0].href;
      }
      return this.httpClient.get<T>(url).pipe(shareReplay());
    }
    return of();
  }

  putApiCall<T>(halResponse: HalResponse, key: string, body: any = {}): Observable<T> {
    if (halResponse._links?.hasOwnProperty(key)) {
      let url = halResponse._links[key].href;
      if (Array.isArray(halResponse._links[key])) {
        url = halResponse._links[key][0].href;
      }
      return this.httpClient.put<T>(url, body).pipe(shareReplay());
    }
    return of();
  }

  deleteApiCall<T>(halResponse: HalResponse, key: string): Observable<T> {
    if (halResponse._links?.hasOwnProperty(key)) {
      let url = halResponse._links[key].href;
      if (Array.isArray(halResponse._links[key])) {
        url = halResponse._links[key][0].href;
      }
      return this.httpClient.delete<T>(url).pipe(shareReplay());
    }
    return of();
  }

  delete(deleteLink: DeleteLink | any) {
    return this.httpClient.delete(deleteLink.delete.href).pipe(shareReplay());
  }
}
