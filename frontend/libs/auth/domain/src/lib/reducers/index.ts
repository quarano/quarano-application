import { UserDto } from './../model/user';
import { createReducer, on } from '@ngrx/store';
import { AuthActions } from '../store/action-types';

export const authFeatureKey = 'auth';

// tslint:disable-next-line: no-empty-interface
export interface AuthState {
  user: UserDto;
}

export const initialAuthState: AuthState = {
  user: JSON.parse(localStorage.getItem('user')),
};

export const authReducer = createReducer(
  initialAuthState,

  on(AuthActions.logout, (state, action) => {
    return {};
  }),

  on(AuthActions.login, (state, action) => {
    return { ...state };
  }),

  on(AuthActions.userDataLoaded, (state, action) => {
    return { ...state, user: action.user };
  })
);
