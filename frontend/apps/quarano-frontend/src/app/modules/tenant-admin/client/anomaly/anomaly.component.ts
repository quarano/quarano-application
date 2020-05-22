import { Component, OnInit, Input } from '@angular/core';
import { ActionDto } from '../../../../models/case-action';
import { DateFunctions } from '@quarano-frontend/shared/util';

@Component({
  selector: 'qro-client-action-anomaly',
  templateUrl: './anomaly.component.html',
  styleUrls: ['./anomaly.component.scss']
})
export class AnomalyComponent implements OnInit {
  @Input() actions: ActionDto[];
  constructor() { }

  ngOnInit() {
  }

  getAnomalyDate(date: string): string {
    return DateFunctions.toCustomLocaleDateString(new Date(date));
  }
}
