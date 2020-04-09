import {UserService} from './../../services/user.service';
import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-header-left',
  templateUrl: './header-left.component.html',
  styleUrls: ['./header-left.component.scss']
})
export class HeaderLeftComponent implements OnInit {
  public readonly isFullyAuthenticated$ = this.userService.isFullyAuthenticated$;
  public readonly isHealthDepartmentUser$ = this.userService.isHealthDepartmentUser$;

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

}
