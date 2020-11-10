import { SymptomDto } from './../model/symptom';
import { createAction, props } from '@ngrx/store';

export const load = createAction('[Symptom] Load', props<{ languageKey: string }>());

export const loaded = createAction('[Symptom] Loaded', props<{ symptoms: SymptomDto[] }>());
