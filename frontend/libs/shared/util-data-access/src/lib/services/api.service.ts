import { API_URL } from '@qro/shared/util';
import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { HalResponse } from '../models/hal-response';
import { Observable, of } from 'rxjs';
import { DeleteLink } from '../models/general';

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
      return this.httpClient.get<T>(url);
    }
    return of();
  }

  putApiCall<T>(halResponse: HalResponse, key: string, body: any = {}): Observable<T> {
    if (halResponse._links?.hasOwnProperty(key)) {
      let url = halResponse._links[key].href;
      if (Array.isArray(halResponse._links[key])) {
        url = halResponse._links[key][0].href;
      }
      return this.httpClient.put<T>(url, body);
    }
    return of();
  }

  deleteApiCall<T>(halResponse: HalResponse, key: string): Observable<T> {
    if (halResponse._links?.hasOwnProperty(key)) {
      let url = halResponse._links[key].href;
      if (Array.isArray(halResponse._links[key])) {
        url = halResponse._links[key][0].href;
      }
      return this.httpClient.delete<T>(url);
    }
    return of();
  }

  delete(deleteLink: DeleteLink) {
    return this.httpClient.delete(deleteLink.delete.href);
  }
}
