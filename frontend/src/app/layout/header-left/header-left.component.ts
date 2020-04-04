import { UserService } from './../../services/user.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header-left',
  templateUrl: './header-left.component.html',
  styleUrls: ['./header-left.component.scss']
})
export class HeaderLeftComponent implements OnInit {
  public isAuthenticated$$ = this.userService.isAuthenticated$$;

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

}
