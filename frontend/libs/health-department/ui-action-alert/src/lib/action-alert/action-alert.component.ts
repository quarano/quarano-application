import { Alert, AlertConfiguration, getAlertConfigurations } from '@qro/health-department/domain';
import { Component } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';

@Component({
  selector: 'qro-action-alert',
  templateUrl: './action-alert.component.html',
  styleUrls: ['./action-alert.component.scss'],
})
export class ActionAlertComponent implements ICellRendererAngularComp {
  public params: any;
  public alerts: AlertConfiguration[] = [];

  agInit(params: any): void {
    this.params = params;
    params?.value.forEach((element) => {
      this.alerts.push(this.alertConfigurationFor(element));
    });
  }

  refresh(): boolean {
    return false;
  }

  private alertConfigurationFor(alert: Alert) {
    return getAlertConfigurations().find((c) => c.alert === alert);
  }
}
