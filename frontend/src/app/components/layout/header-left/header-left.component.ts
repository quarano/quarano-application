import { UserService } from '@services/user.service';
import { Component, OnInit } from '@angular/core';
import { roleNames } from '@models/role';

@Component({
  selector: 'app-header-left',
  templateUrl: './header-left.component.html',
  styleUrls: ['./header-left.component.scss']
})
export class HeaderLeftComponent implements OnInit {
  public readonly enrollmentCompleted$ = this.userService.enrollmentCompleted$;
  public readonly isLoggedIn$ = this.userService.isLoggedIn$;
  public rolesNames = roleNames;

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

}
