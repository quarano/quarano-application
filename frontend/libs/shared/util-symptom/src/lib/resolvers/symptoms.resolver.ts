import { selectedLanguage } from './../../../../util-translation/src/lib/store/language.selectors';
import { languageFeatureKey } from './../../../../util-translation/src/lib/reducers/index';
import { LanguageSelectors } from '@qro/shared/util-translation';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { filter, first, switchMap, tap, withLatestFrom } from 'rxjs/operators';
import { SymptomSelectors } from '../store/selector-types';
import { SymptomActions } from '../store/action-types';

@Injectable()
export class SymptomsResolver implements Resolve<boolean> {
  constructor(private store: Store) {}

  resolve(route: ActivatedRouteSnapshot): Observable<boolean> {
    return this.store.pipe(
      select(SymptomSelectors.loaded),
      tap((loaded) => {
        if (!loaded) {
          this.store.dispatch(SymptomActions.load({ languageKey: null }));
        }
      }),
      filter((loaded) => !!loaded),
      first()
    );
  }
}
