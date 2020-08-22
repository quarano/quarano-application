import { EnrollmentStatusDto } from './../model/enrollment-status';
import { Injectable } from '@angular/core';
import { Observable, combineLatest } from 'rxjs';
import { Store, select } from '@ngrx/store';
import { ClientSelectors } from './selector-types';
import { filter, map, tap, first } from 'rxjs/operators';
import { ClientActions } from './action-types';

@Injectable()
export class ClientStore {
  private _enrollmentStatus$: Observable<EnrollmentStatusDto>;
  private _isLoaded$: Observable<boolean>;

  get enrollmentStatus$(): Observable<EnrollmentStatusDto> {
    return this._enrollmentStatus$;
  }

  get isLoaded$(): Observable<boolean> {
    return this._isLoaded$;
  }

  constructor(private store: Store<ClientStore>) {
    this._enrollmentStatus$ = this.store.pipe(select(ClientSelectors.selectEnrollmentStatus));
    this._isLoaded$ = this.store.pipe(select(ClientSelectors.isEnrollmentStatusLoaded));
  }

  public loadEnrollmentStatus() {
    this.store.dispatch(ClientActions.loadEnrollmentStatus());
  }

  public completeEnrollment(withoutEncounters: boolean) {
    this.store.dispatch(ClientActions.completeEnrollment({ withoutEncounters }));
  }
}
