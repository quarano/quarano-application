import { SymptomDto } from './../model/symptom';
import { LanguageSelectors } from '@qro/shared/util-translation';
import { select, Store } from '@ngrx/store';
import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { API_URL } from '@qro/shared/util-data-access';
import { Observable } from 'rxjs';
import { first, shareReplay, switchMap } from 'rxjs/operators';

@Injectable()
export class SymptomService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string, private store: Store) {}

  getSymptoms(languageKey: string): Observable<SymptomDto[]> {
    let uri = `${this.apiUrl}/api/symptoms`;
    if (!languageKey) {
      return this.store.pipe(
        select(LanguageSelectors.selectedLanguage),
        first(),
        switchMap((selectedLanguage) => {
          if (selectedLanguage) {
            uri += `?locale=${selectedLanguage.key}`;
          }
          return this.loadSymptoms(uri);
        })
      );
    }
    uri += `?locale=${languageKey}`;
    return this.loadSymptoms(uri);
  }

  private loadSymptoms(uri: string): Observable<SymptomDto[]> {
    return this.httpClient.get<SymptomDto[]>(uri).pipe(shareReplay());
  }
}
