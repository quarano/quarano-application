import { Component, OnInit } from '@angular/core';
import { UserService } from '@services/user.service';
import {MatDialog} from '@angular/material/dialog';
import {HdContactComponent} from '../../hd-contact/hd-contact.component';

@Component({
  selector: 'app-header-right',
  templateUrl: './header-right.component.html',
  styleUrls: ['./header-right.component.scss']
})
export class HeaderRightComponent implements OnInit {
  public isLoggedIn$ = this.userService.isLoggedIn$;
  public currentUserName$ = this.userService.currentUserName$;

  constructor(
    private userService: UserService, private matDialog: MatDialog) { }

  ngOnInit() {
  }

  logout() {
    this.userService.logout();
  }

  showContact() {
    this.matDialog.open(HdContactComponent);
  }
}
