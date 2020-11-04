import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { StaticPageActions } from './action-types';
import { map, switchMap, shareReplay } from 'rxjs/operators';
import { API_URL } from '@qro/shared/util-data-access';

@Injectable()
export class StaticPageEffects {
  constructor(private actions$: Actions, private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}
  private baseUrl = `${this.apiUrl}`;

  loadStaticPages$ = createEffect(() =>
    this.actions$.pipe(
      ofType(StaticPageActions.loadStaticPages),
      switchMap((action) => this.httpClient.get<any>(`${this.baseUrl}/frontendtexts`).pipe(shareReplay())),
      map((dto) => StaticPageActions.staticPagesLoaded({ staticPages: dto._embedded.texts }))
    )
  );
}
