import { Component } from '@angular/core';
import { ProgressBarService } from './services/progress-bar.service';
import { TenantService } from './services/tenant.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  public progressBarActive$$ = this.progressBarService.progressBarActive$$;
  public tenant$$ = this.tenantService.tenant$$;

  constructor(
    private tenantService: TenantService,
    private progressBarService: ProgressBarService) {
  }
}
