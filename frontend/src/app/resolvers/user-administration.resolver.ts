import { ApiService } from '@services/api.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { UserListItemDto } from '@models/user';

@Injectable()
export class UserAdministrationResolver implements Resolve<UserListItemDto[]> {
  constructor(private apiService: ApiService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<UserListItemDto[]> {
    return this.apiService.getHealthDepartmentUsers();
  }
}
