import { createReducer, on } from '@ngrx/store';
import { AuthActions } from '../store/action-types';

export const authFeatureKey = 'auth';

// tslint:disable-next-line: no-empty-interface
export interface AuthState {}

export const initialAuthState: AuthState = {};

export const authReducer = createReducer(
  initialAuthState,

  on(AuthActions.logout, (state, action) => {
    return {};
  })
);
