import { HdContactComponent } from '@qro/client/api';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { HealthDepartmentDto, HealthDepartmentService } from '@qro/health-department/api';
import { UserService } from '@qro/auth/api';
import { map } from 'rxjs/operators';

@Component({
  selector: 'qro-header-right',
  templateUrl: './header-right.component.html',
  styleUrls: ['./header-right.component.scss'],
})
export class HeaderRightComponent implements OnInit {
  public isLoggedIn$ = this.userService.isLoggedIn$;
  public healthDepartment$: Observable<HealthDepartmentDto> = this.healthDepartmentService.healthDepartment$;

  public get currentUserName$(): Observable<string> {
    return this.userService.user$.pipe(
      map((user) => {
        if (user) {
          if (this.userService.isHealthDepartmentUser) {
            if (user.firstName && user.lastName) {
              return `${user.firstName} ${user.lastName} (${
                user.healthDepartment?.name || 'Gesundheitsamt unbekannt'
              })`;
            }
            return `${user.username} (${user.healthDepartment?.name || 'Gesundheitsamt unbekannt'})`;
          } else if (user.client?.firstName || user.client?.lastName) {
            return `${user.client.firstName || ''} ${user.client.lastName || ''}`;
          }
          return user.username;
        }
        return null;
      })
    );
  }

  constructor(
    private userService: UserService,
    private healthDepartmentService: HealthDepartmentService,
    private matDialog: MatDialog
  ) {}

  ngOnInit() {}

  logout() {
    this.userService.logout();
  }

  showContact(department: HealthDepartmentDto) {
    this.matDialog.open(HdContactComponent, { data: department });
  }
}
