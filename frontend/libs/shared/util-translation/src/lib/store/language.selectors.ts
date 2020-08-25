import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ILanguageConfig } from '../model/language-config';
import { LanguageState, languageFeatureKey } from '../reducers';

export const selectLanguageState = createFeatureSelector<LanguageState>(languageFeatureKey);

export const selectedLanguage = createSelector(selectLanguageState, (lang) => lang.selectedLanguage);

export const supportedLanguages = createSelector(selectLanguageState, (lang) => lang.supportedLanguages);

export const defaultLanguage = createSelector(supportedLanguages, (langs) => langs.find((l) => l.isDefaultLanguage));

export const supportedLanguageByKey = createSelector(
  supportedLanguages,
  defaultLanguage,
  (languages: ILanguageConfig[], defaultLang: ILanguageConfig, props: { key: string }) =>
    languages.find((l) => l.key === props.key) || defaultLang
);
