import { EnrollmentStatusDto } from './../model/enrollment-status';
import { createAction, props } from '@ngrx/store';

export const loadEnrollmentStatus = createAction('[Enrollment Status] Load');
export const enrollmentStatusLoaded = createAction(
  '[Enrollment Status] Loaded',
  props<{ enrollmentStatus: EnrollmentStatusDto }>()
);
export const completeEnrollment = createAction('[Enrollment Status] Complete', props<{ withoutEncounters: boolean }>());
