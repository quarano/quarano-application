import { TenantService } from './../../services/tenant.service';
import { Component, OnInit } from '@angular/core';
import { TenantsEnum } from 'src/app/services/tenantsEnum';

@Component({
  selector: 'app-header-right',
  templateUrl: './header-right.component.html',
  styleUrls: ['./header-right.component.scss']
})
export class HeaderRightComponent implements OnInit {
  public tenantsEnum = TenantsEnum;
  public urlTenant = this.tenantService.urlTenant;
  public tenant$$ = this.tenantService.tenant$$;

  constructor(private tenantService: TenantService) { }

  ngOnInit() {
  }

  public tenantLogout() {
    this.tenantService.logout();
  }
}
