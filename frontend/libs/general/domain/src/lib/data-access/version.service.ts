import { VersionDto } from './../model/version';
import { API_URL } from '@qro/shared/util-data-access';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { shareReplay } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class VersionService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  getVersion(): Observable<VersionDto> {
    const uri = `${this.apiUrl}/actuator/info`;
    return this.httpClient.get<VersionDto>(uri).pipe(shareReplay());
  }
}
