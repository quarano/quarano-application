import { Component, OnInit } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {HdContactComponent} from '../../hd-contact/hd-contact.component';
import {Observable} from 'rxjs';
import {HealthDepartmentDto} from '../../../models/healthDepartment';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'qro-header-right',
  templateUrl: './header-right.component.html',
  styleUrls: ['./header-right.component.scss']
})
export class HeaderRightComponent implements OnInit {
  public isLoggedIn$ = this.userService.isLoggedIn$;
  public currentUserName$ = this.userService.currentUserName$;
  public healthDepartment$: Observable<HealthDepartmentDto> = this.userService.healthDepartment$;

  constructor(
    private userService: UserService, private matDialog: MatDialog) { }

  ngOnInit() {
  }

  logout() {
    this.userService.logout();
  }

  showContact(department: HealthDepartmentDto) {
    this.matDialog.open(HdContactComponent, {data: department});
  }
}
