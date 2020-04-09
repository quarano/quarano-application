import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-header-right',
  templateUrl: './header-right.component.html',
  styleUrls: ['./header-right.component.scss']
})
export class HeaderRightComponent implements OnInit {
  public healthDepartment$ = this.userService.healthDepartment$;
  public isLoggedIn$ = this.userService.isLoggedIn$;

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

  public logout() {
    this.userService.logout();
  }
}
