import { createAction, props } from '@ngrx/store';
import { ILanguageConfig } from '../model/language-config';

export const languageSelectedAuthenticatedUser = createAction(
  '[Top Menu] Language Selected (authenticated user)',
  props<{ selectedLanguage: ILanguageConfig }>()
);

export const languageSelectedAnonymousUser = createAction(
  '[Top Menu] Language Selected (anonymous user)',
  props<{ selectedLanguage: ILanguageConfig }>()
);

export const contentLanguageHeaderRead = createAction(
  '[Login] Content Language Header Read',
  props<{ selectedLanguage: ILanguageConfig }>()
);

export const supportedLanguagesLoaded = createAction(
  '[App Component] Supported Languages Loaded',
  props<{ supportedLanguages: ILanguageConfig[] }>()
);
