import { API_URL } from '@qro/shared/util-data-access';
import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { Observable } from 'rxjs';
import { SymptomDto } from '../model/symptom';
import { shareReplay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class SymptomService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  getSymptoms(): Observable<SymptomDto[]> {
    return this.httpClient.get<SymptomDto[]>(`${this.apiUrl}/api/symptoms`).pipe(shareReplay());
  }
}
