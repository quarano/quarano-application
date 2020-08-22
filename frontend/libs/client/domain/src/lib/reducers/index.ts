import { EnrollmentStatusDto, getEmptyEnrollmentStatus } from './../model/enrollment-status';
import { createReducer, on } from '@ngrx/store';
import { ClientActions } from '../store/action-types';

export interface ClientState {
  enrollmentStatus: EnrollmentStatusDto;
  isEnrollmentStatusLoaded: boolean;
}

export const clientFeatureKey = 'client';

export const initialClientState: ClientState = {
  enrollmentStatus: getEmptyEnrollmentStatus(),
  isEnrollmentStatusLoaded: false,
};

export const clientReducer = createReducer(
  initialClientState,

  on(ClientActions.completeEnrollment, (state) => {
    return state;
  }),

  on(ClientActions.loadEnrollmentStatus, (state) => ({
    ...state,
    isEnrollmentStatusLoaded: false,
  })),

  on(ClientActions.enrollmentStatusLoaded, (state, { enrollmentStatus }) => {
    return {
      ...state,
      isEnrollmentStatusLoaded: true,
      enrollmentStatus,
    };
  })
);
