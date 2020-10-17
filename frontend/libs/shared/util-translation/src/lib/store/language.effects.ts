import { SymptomEntityService } from '@qro/shared/util-symptom';
import { DateAdapter } from '@angular/material/core';
import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType, act } from '@ngrx/effects';
import { tap } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { LanguageActions } from './action-types';

@Injectable()
export class LanguageEffects {
  languageSelected$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(LanguageActions.languageSelected, LanguageActions.contentLanguageHeaderRead),
        tap((action) => this.translate.use(action.selectedLanguage.key)),
        tap((action) => localStorage.setItem('selectedLanguage', JSON.stringify(action.selectedLanguage))),
        tap((action) => this.dateAdapter.setLocale(action.selectedLanguage.key)),
        tap((action) => this.symptomEntityService.load())
      ),
    { dispatch: false }
  );

  constructor(
    private actions$: Actions,
    private translate: TranslateService,
    private dateAdapter: DateAdapter<any>,
    private symptomEntityService: SymptomEntityService
  ) {}
}
