import { API_URL } from '@qro/shared/util';
import { HttpClient } from '@angular/common/http';
import { ChangePasswordDto } from './../models/change-password';
import { Injectable, Inject } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) { }

  changePassword(dto: ChangePasswordDto) {
    return this.httpClient.put(`${this.apiUrl}/api/user/me/password`, dto);
  }

  checkUsername(username: string): Observable<any> {
    return this.httpClient.get(`${this.apiUrl}/api/registration/checkusername/${username}`);
  }

  login(username: string, password: string): Observable<{ token: string }> {
    return this.httpClient.post<{ token: string }>(`${this.apiUrl}/login`, { username, password });
  }
}
