import { API_URL } from '@qro/shared/util-data-access';
import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountListDto, AccountDto } from '../model/account';
import { share, map, shareReplay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  getAccountList(): Observable<AccountListDto> {
    return this.httpClient.get<any>(`${this.apiUrl}/api/hd/accounts`).pipe(
      shareReplay(),
      map((result) => result._embedded)
    );
  }

  getAccountDetail(id: string): Observable<AccountDto> {
    return this.httpClient.get<AccountDto>(`${this.apiUrl}/api/hd/accounts/${id}`).pipe(shareReplay());
  }

  createAccount(account: AccountDto): Observable<AccountDto> {
    return this.httpClient.post<AccountDto>(`${this.apiUrl}/api/hd/accounts`, account).pipe(shareReplay());
  }

  editAccount(account: AccountDto): Observable<AccountDto> {
    return this.httpClient
      .put<AccountDto>(`${this.apiUrl}/api/hd/accounts/${account.accountId}`, account)
      .pipe(shareReplay());
  }
}
