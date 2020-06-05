import { HdContactComponent } from '@qro/client/api';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { HealthDepartmentDto, HealthDepartmentService } from '@qro/health-department/api';
import { UserService } from '@qro/auth/api';
import { map, distinctUntilChanged } from 'rxjs/operators';
import { EnrollmentService } from '@qro/client/enrollment/api';

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
    public enrollmentService: EnrollmentService,
    private matDialog: MatDialog
  ) {}

  logout() {
    this.userService.logout();
  }

  showContact(department: HealthDepartmentDto) {
    this.matDialog.open(HdContactComponent, { data: department });
  }
}
