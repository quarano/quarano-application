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
        ofType(LanguageActions.languageSelected),
        tap((action) => {
          this.translate.use(action.selectedLanguage.key);
          localStorage.setItem('selectedLanguage', JSON.stringify(action.selectedLanguage));
        }),
        tap((action) => this.dateAdapter.setLocale(action.selectedLanguage.key))
      ),
    { dispatch: false }
  );

  constructor(private actions$: Actions, private translate: TranslateService, private dateAdapter: DateAdapter<any>) {}
}
