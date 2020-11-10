import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { filter, first, tap } from 'rxjs/operators';
import { StaticPageSelectors } from '../store/selector-types';
import { StaticPageActions } from '../store/action-types';

@Injectable()
export class StaticPagesResolver implements Resolve<boolean> {
  constructor(private store: Store) {}

  resolve(route: ActivatedRouteSnapshot): Observable<boolean> {
    return this.store.pipe(
      select(StaticPageSelectors.areStaticPagesLoaded),
      tap((loaded) => {
        if (!loaded) {
          this.store.dispatch(StaticPageActions.loadStaticPages());
        }
      }),
      filter((loaded) => !!loaded),
      first()
    );
  }
}
