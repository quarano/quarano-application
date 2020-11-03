import { StaticPageKeys, StaticPageDto } from './../model/static-page';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Store, select } from '@ngrx/store';
import { StaticPageSelectors } from './selector-types';
import { filter, tap, first, switchMap } from 'rxjs/operators';
import { StaticPageActions } from './action-types';

@Injectable()
export class StaticPageStore {
  private _isLoaded$: Observable<boolean>;

  get isLoaded$(): Observable<boolean> {
    return this._isLoaded$;
  }

  constructor(private store: Store<StaticPageStore>) {
    this._isLoaded$ = this.store.pipe(select(StaticPageSelectors.areStaticPagesLoaded));
  }

  public loadStaticPages() {
    this.store.dispatch(StaticPageActions.loadStaticPages());
  }

  public getStaticPageByKey(key: StaticPageKeys): Observable<StaticPageDto> {
    const getPageFromStore$ = this.store.pipe(select(StaticPageSelectors.selectStaticPageByKey, { key }));

    return this.isLoaded$.pipe(
      tap((loaded) => {
        if (!loaded) {
          this.loadStaticPages();
        }
      }),
      filter((loaded) => !!loaded),
      switchMap((_) => getPageFromStore$),
      first()
    );
  }
}
