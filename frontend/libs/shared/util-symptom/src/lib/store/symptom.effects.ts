import { SymptomService } from './../data-access/symptom.service';
import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { switchMap } from 'rxjs/operators';
import { LanguageActions } from '@qro/shared/util-translation';
import { SymptomActions } from './action-types';

@Injectable()
export class SymptomEffects {
  languageSelected$ = createEffect(() =>
    this.actions$.pipe(
      ofType(LanguageActions.languageSelected),
      switchMap((action) => [SymptomActions.load({ languageKey: action.selectedLanguage.key })])
    )
  );

  load$ = createEffect(() =>
    this.actions$.pipe(
      ofType(SymptomActions.load),
      switchMap((action) => this.service.getSymptoms(action.languageKey)),
      switchMap((symptoms) => [SymptomActions.loaded({ symptoms })])
    )
  );

  constructor(private actions$: Actions, private service: SymptomService) {}
}
