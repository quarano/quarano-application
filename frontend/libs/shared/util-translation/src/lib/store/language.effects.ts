import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
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
        })
      ),
    { dispatch: false }
  );

  constructor(private actions$: Actions, private translate: TranslateService) {}
}
