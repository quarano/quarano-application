import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { ClientActions } from './action-types';
import { map, switchMap, shareReplay } from 'rxjs/operators';
import { API_URL } from '@qro/shared/util-data-access';
import { EnrollmentStatusDto } from '../model/enrollment-status';

@Injectable()
export class ClientEffects {
  constructor(private actions$: Actions, private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}
  private baseUrl = `${this.apiUrl}/api`;

  loadEnrollmentStatus$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ClientActions.loadEnrollmentStatus),
      switchMap((action) => this.httpClient.get<EnrollmentStatusDto>(`${this.baseUrl}/enrollment`).pipe(shareReplay())),
      map((status) => ClientActions.enrollmentStatusLoaded({ enrollmentStatus: status }))
    )
  );

  completeEnrollmentStatus$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ClientActions.completeEnrollment),
      switchMap((action) =>
        this.httpClient
          .post(`${this.baseUrl}/enrollment/completion?withoutEncounters=${action.withoutEncounters}`, {})
          .pipe(shareReplay())
      ),
      map((_) => ClientActions.loadEnrollmentStatus())
    )
  );
}
