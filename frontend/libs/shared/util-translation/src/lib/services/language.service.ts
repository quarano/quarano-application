import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { tap, switchMap, take, shareReplay } from 'rxjs/operators';
import { Store, select } from '@ngrx/store';
import { ILanguageConfig } from '../model/language-config';
import { LanguageActions } from '../store/action-types';
import { LanguageSelectors } from '../store/selector-types';

@Injectable({
  providedIn: 'root',
})
export class LanguageService {
  constructor(private client: HttpClient, private store: Store, private translate: TranslateService) {}

  public init(browserLanguage: string): Observable<ILanguageConfig> {
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
      switchMap((languages) => {
        return this.store.pipe(
          select(LanguageSelectors.defaultLanguage),
          take(1),
          tap((defaultLang) => this.translate.setDefaultLang(defaultLang.key))
        );
      }),
      switchMap((defaultLang) => {
        return this.store.pipe(
          select(LanguageSelectors.supportedLanguageByKey, {
            key: browserLanguage,
          }),
          take(1),
          tap((language) =>
            this.store.dispatch(
              LanguageActions.languageSelected({
                selectedLanguage: language,
              })
            )
          )
        );
      })
    );
  }
}
