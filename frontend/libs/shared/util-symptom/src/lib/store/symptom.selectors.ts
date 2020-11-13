import { SYMPTOM_FEATURE_KEY } from './../reducers/index';
import { createFeatureSelector, createSelector } from '@ngrx/store';
import { SymptomState } from '../reducers';

export const selectSymptpomState = createFeatureSelector<SymptomState>(SYMPTOM_FEATURE_KEY);

export const symptoms = createSelector(selectSymptpomState, (state) => state.symptoms);

export const characteristicSymptoms = createSelector(selectSymptpomState, (state) =>
  state.symptoms.filter((s) => s.characteristic)
);

export const loaded = createSelector(selectSymptpomState, (state) => state.loaded);
