import { UserListItemDto } from '@models/user';
import { ApiService } from '@services/api.service';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';

@Injectable()
export class UserResolver implements Resolve<UserListItemDto> {
  constructor(private apiService: ApiService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<UserListItemDto> {
    const id = route.paramMap.get('id');

    if (id) {
      return this.apiService.getHealthDepartmentUser(id);
    } else {
      return of(
        {
          id: null,
          firstName: null,
          lastName: null,
          username: null,
          _links: null,
          roles: []
        });
    }
  }
}
