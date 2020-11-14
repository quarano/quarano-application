import { AccountDto } from './../model/account';
import { ACCOUNT_FEATURE_KEY } from './account-entity.service';
import { Injectable, Inject } from '@angular/core';
import { DefaultDataService, HttpUrlGenerator } from '@ngrx/data';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { API_URL } from '@qro/shared/util-data-access';
import { Update } from '@ngrx/entity';

@Injectable()
export class AccountDataService extends DefaultDataService<AccountDto> {
  constructor(http: HttpClient, httpUrlGenerator: HttpUrlGenerator, @Inject(API_URL) private apiUrl: string) {
    super(ACCOUNT_FEATURE_KEY, http, httpUrlGenerator, { root: `${apiUrl}/hd` });
  }

  getAll(): Observable<AccountDto[]> {
    return this.execute('GET', this.entitiesUrl).pipe(
      shareReplay(1),
      map((result) => {
        if (result?._embedded?.accounts) {
          return result._embedded.accounts;
        } else {
          return [];
        }
      })
    );
  }

  getById(key: number | string): Observable<AccountDto> {
    let err: Error | undefined;
    if (key == null) {
      err = new Error(`No "${this.entityName}" key to get`);
    }
    return this.execute('GET', this.entitiesUrl + key, err).pipe(shareReplay(1));
  }

  update(update: Update<AccountDto>): Observable<AccountDto> {
    const id = update && update.id;
    const updateOrError = id == null ? new Error(`No "${this.entityName}" update data or id`) : update.changes;
    return this.execute('PUT', this.entitiesUrl + id, updateOrError).pipe(shareReplay(1));
  }

  add(entity: AccountDto): Observable<AccountDto> {
    const entityOrError = entity || new Error(`No "${this.entityName}" entity to add`);
    return this.execute('POST', this.entitiesUrl, entityOrError).pipe(shareReplay(1));
  }
}
