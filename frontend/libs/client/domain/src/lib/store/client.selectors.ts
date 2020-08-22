import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ClientState, clientFeatureKey } from '../reducers';

const selectClientState = createFeatureSelector<ClientState>(clientFeatureKey);

export const selectEnrollmentStatus = createSelector(selectClientState, (client) => client?.enrollmentStatus);

export const isEnrollmentStatusLoaded = createSelector(selectClientState, (client) => client?.isEnrollmentStatusLoaded);
