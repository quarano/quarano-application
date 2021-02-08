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
      const config = this.alertConfigurationFor(element);
      if (config) {
        this.alerts.push(this.alertConfigurationFor(element));
      }
    });
  }

  refresh(): boolean {
    return false;
  }

  private alertConfigurationFor(alert: Alert) {
    const config = getAlertConfigurations().find((c) => c.alert === alert);
    if (!config) {
      console.warn(`No alert configuration found for alert '${alert}'. Alert will be ignored`);
    }
    return config;
  }
}
