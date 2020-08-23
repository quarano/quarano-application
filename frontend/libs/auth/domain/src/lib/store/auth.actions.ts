import { UserDto } from './../model/user';
import { createAction, props } from '@ngrx/store';

export const logout = createAction('[Auth] Logout');

export const login = createAction('[Auth] Login');

export const userDataLoaded = createAction('[Auth] User data loaded', props<{ user: UserDto }>());
