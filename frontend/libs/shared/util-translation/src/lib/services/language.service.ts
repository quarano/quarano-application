import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';
import { Observable, iif, of } from 'rxjs';
import { tap, switchMap, take, shareReplay, map } from 'rxjs/operators';
import { Store, select } from '@ngrx/store';
import { ILanguageConfig } from '../model/language-config';
import { LanguageActions } from '../store/action-types';
import { LanguageSelectors } from '../store/selector-types';

@Injectable({
  providedIn: 'root',
})
export class LanguageService {
  constructor(private client: HttpClient, private store: Store, private translate: TranslateService) {}

  public init(): Observable<ILanguageConfig> {
    return this.client.get<ILanguageConfig[]>('./assets/i18n/supported-languages.json').pipe(
      shareReplay(),
      tap((languages) => {
        this.store.dispatch(
          LanguageActions.supportedLanguagesLoaded({
            supportedLanguages: languages,
          })
        );
      }),
      tap((languages) => this.translate.addLangs([...languages.map((l) => l.key)])),
      tap((languages) => this.translate.setDefaultLang(languages.find((l) => l.isDefaultLanguage).key)),
      switchMap((languages) => {
        return this.store.pipe(
          select(LanguageSelectors.selectedLanguage),
          map((selectedLanguage) => {
            if (selectedLanguage) {
              return selectedLanguage;
            }
            let lang = languages.find((l) => l.key === this.translate.getBrowserLang());
            if (!lang) {
              lang = languages.find((l) => l.isDefaultLanguage);
            }
            this.store.dispatch(
              LanguageActions.languageSelected({
                selectedLanguage: lang,
              })
            );
            return lang;
          })
        );
      })
    );
  }
}
