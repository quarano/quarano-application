import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { filter, first, tap } from 'rxjs/operators';
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
