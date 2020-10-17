import { SymptomDto } from './../model/symptom';
import { createReducer, on } from '@ngrx/store';
import { SymptomActions } from '../store/action-types';

export const SYMPTOM_FEATURE_KEY = 'Symptom';

export interface SymptomState {
  symptoms: SymptomDto[];
  loaded: boolean;
}

export const initialSymptomState: SymptomState = {
  symptoms: [],
  loaded: false,
};

export const symptomReducer = createReducer(
  initialSymptomState,

  on(SymptomActions.load, (state, action) => {
    return { ...state, loaded: false };
  }),

  on(SymptomActions.loaded, (state, action) => {
    return { ...state, symptoms: action.symptoms, loaded: true };
  })
);
