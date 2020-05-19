import { API_URL } from '@quarano-frontend/shared/util';
import { HttpClient } from '@angular/common/http';
import { ChangePasswordDto } from './../models/change-password';
import { Injectable, Inject } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) { }

  changePassword(dto: ChangePasswordDto) {
    return this.httpClient.put(`${this.apiUrl}/api/user/me/password`, dto);
  }

}
