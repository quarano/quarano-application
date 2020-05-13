import {Alert} from '@models/action';
import {Component, Input, OnInit} from '@angular/core';

export interface AlertConfiguration {
  alert: Alert;
  color: string;
  icon: string;
  displayName: string;
  order: number;
}

@Component({
  selector: 'app-action-alert',
  templateUrl: './action-alert.component.html',
  styleUrls: ['./action-alert.component.scss']
})
export class ActionAlertComponent implements OnInit {
  @Input() alert: AlertConfiguration;

  constructor() { }

  ngOnInit() {
  }
}
