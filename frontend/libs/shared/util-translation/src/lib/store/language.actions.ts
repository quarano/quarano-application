import { createAction, props } from '@ngrx/store';
import { ILanguageConfig } from '../model/language-config';

export const languageSelected = createAction(
  '[Top Menu] Language Selected',
  props<{ selectedLanguage: ILanguageConfig }>()
);

export const supportedLanguagesLoaded = createAction(
  '[App Component] Supported Languages Loaded',
  props<{ supportedLanguages: ILanguageConfig[] }>()
);
