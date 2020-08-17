import { HdContactComponent } from '@qro/client/api';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { HealthDepartmentService } from '@qro/health-department/api';
import { HealthDepartmentDto, UserService } from '@qro/auth/api';
import { ClientStore } from '@qro/client/api';

@Component({
  selector: 'qro-header-right',
  templateUrl: './header-right.component.html',
  styleUrls: ['./header-right.component.scss'],
})
export class HeaderRightComponent {
  public healthDepartment$: Observable<HealthDepartmentDto> = this.healthDepartmentService.healthDepartment$;
  public currentUserName$ = this.userService.currentUserName$;

  constructor(
    public userService: UserService,
    private healthDepartmentService: HealthDepartmentService,
    public clientStore: ClientStore,
    private matDialog: MatDialog
  ) {}

  logout() {
    this.userService.logout();
  }

  showContact(department: HealthDepartmentDto) {
    this.matDialog.open(HdContactComponent, { data: department, maxWidth: 600 });
  }
}
