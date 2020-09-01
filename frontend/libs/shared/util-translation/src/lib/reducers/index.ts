import { ILanguageConfig } from './../model/language-config';
import { createReducer, on } from '@ngrx/store';
import { LanguageActions } from '../store/action-types';

export interface LanguageState {
  selectedLanguage: ILanguageConfig;
  supportedLanguages: ILanguageConfig[];
}

export const languageFeatureKey = 'language';

export const initialLanguageState: LanguageState = {
  selectedLanguage: JSON.parse(localStorage.getItem('selectedLanguage')) || undefined,
  supportedLanguages: [],
};

export const languageReducer = createReducer(
  initialLanguageState,

  on(LanguageActions.languageSelected, (state, action) => {
    return {
      ...state,
      selectedLanguage: action.selectedLanguage,
    };
  }),

  on(LanguageActions.supportedLanguagesLoaded, (state, action) => {
    return {
      ...state,
      supportedLanguages: action.supportedLanguages,
    };
  })
);
