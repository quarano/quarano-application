import { createFeatureSelector, createSelector } from '@ngrx/store';
import { AuthState, authFeatureKey } from '../reducers';

export const selectAuthState = createFeatureSelector<AuthState>(authFeatureKey);

export const user = createSelector(selectAuthState, (auth) => auth.user);
