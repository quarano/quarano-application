import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { ProgressBarService } from 'src/app/services/progress-bar.service';
import { TenantsEnum } from 'src/app/services/tenantsEnum';
import { TenantService } from 'src/app/services/tenant.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  public progressBarActive$$ = this.progressBarService.progressBarActive$$;
  public isAuthenticated$$ = this.userService.isAuthenticated$$;
  public tenantsEnum = TenantsEnum;
  public urlTenant = this.tenantService.urlTenant;
  public tenant$$ = this.tenantService.tenant$$;

  constructor(
    private userService: UserService,
    private progressBarService: ProgressBarService,
    private tenantService: TenantService
  ) { }

  ngOnInit() {
  }

  public tenantLogout() {
    this.tenantService.logout();
  }

}
