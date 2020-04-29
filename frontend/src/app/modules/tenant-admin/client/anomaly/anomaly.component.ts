import { Component, OnInit, Input } from '@angular/core';
import { ActionDto } from '@models/case-action';

@Component({
  selector: 'app-client-action-anomaly',
  templateUrl: './anomaly.component.html',
  styleUrls: ['./anomaly.component.scss']
})
export class AnomalyComponent implements OnInit {
  @Input() actions: ActionDto[];
  constructor() { }

  ngOnInit() {
  }

  getAnomalyDate(date: string): string {
    return new Date(date).toCustomLocaleDateString();
  }

  getItemTime(date: string): string {
    return new Date(date + 'Z').toLocaleTimeString('de-DE', { hour: '2-digit', minute: '2-digit' });
  }
}
