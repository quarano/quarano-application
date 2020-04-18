import { Component, OnInit } from '@angular/core';
import { UserService } from '@services/user.service';

@Component({
  selector: 'app-header-right',
  templateUrl: './header-right.component.html',
  styleUrls: ['./header-right.component.scss']
})
export class HeaderRightComponent implements OnInit {
  public isLoggedIn$ = this.userService.isLoggedIn$;
  public isFullyAuthenticated$ = this.userService.enrollmentCompleted$;
  public currentUserName$ = this.userService.currentUserName$;

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

  public logout() {
    this.userService.logout();
  }
}
