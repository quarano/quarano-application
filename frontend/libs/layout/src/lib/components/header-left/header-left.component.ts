import { Component, OnInit } from '@angular/core';
import { roleNames, UserService } from '@qro/auth/api';

@Component({
  selector: 'qro-header-left',
  templateUrl: './header-left.component.html',
  styleUrls: ['./header-left.component.scss'],
})
export class HeaderLeftComponent implements OnInit {
  public readonly isLoggedIn$ = this.userService.isLoggedIn$;
  public rolesNames = roleNames;

  constructor(private userService: UserService) {}

  ngOnInit() {}
}
