import { switchMap } from 'rxjs/operators';
import { LanguageSelectors } from '@qro/shared/util-translation';
import { select, Store } from '@ngrx/store';
import { SYMPTOM_FEATURE_KEY } from './symptom-entity.service';
import { SymptomDto } from './../model/symptom';
import { Injectable, Inject } from '@angular/core';
import { DefaultDataService, HttpUrlGenerator } from '@ngrx/data';
import { HttpClient } from '@angular/common/http';
import { API_URL } from '@qro/shared/util-data-access';
import { Observable } from 'rxjs';

@Injectable()
export class SymptomDataService extends DefaultDataService<SymptomDto> {
  constructor(
    http: HttpClient,
    httpUrlGenerator: HttpUrlGenerator,
    @Inject(API_URL) apiUrl: string,
    private appStore: Store
  ) {
    super(SYMPTOM_FEATURE_KEY, http, httpUrlGenerator, { root: `${apiUrl}/api` });
  }

  getAll(): Observable<SymptomDto[]> {
    return this.appStore.pipe(
      select(LanguageSelectors.selectedLanguage),
      switchMap((selectedLanguage) => this.execute('GET', this.entitiesUrl + `?locale=${selectedLanguage.key}`))
    );
  }
}
