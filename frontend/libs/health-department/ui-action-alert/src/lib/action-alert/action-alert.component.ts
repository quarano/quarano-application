import { AlertConfiguration } from '@quarano-frontend/health-department/domain';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'qro-action-alert',
  templateUrl: './action-alert.component.html',
  styleUrls: ['./action-alert.component.scss']
})
export class ActionAlertComponent implements OnInit {
  @Input() alert: AlertConfiguration;

  constructor() { }

  ngOnInit(): void {
  }

}
