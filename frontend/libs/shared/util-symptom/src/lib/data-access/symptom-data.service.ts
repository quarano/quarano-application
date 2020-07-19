import { SYMPTOM_FEATURE_KEY } from './symptom-entity.service';
import { SymptomDto } from './../model/symptom';
import { Injectable, Inject } from '@angular/core';
import { DefaultDataService, HttpUrlGenerator } from '@ngrx/data';
import { HttpClient } from '@angular/common/http';
import { API_URL } from '@qro/shared/util-data-access';

@Injectable()
export class SymptomDataService extends DefaultDataService<SymptomDto> {
  constructor(http: HttpClient, httpUrlGenerator: HttpUrlGenerator, @Inject(API_URL) apiUrl: string) {
    super(SYMPTOM_FEATURE_KEY, http, httpUrlGenerator, { root: `${apiUrl}/api` });
  }
}
