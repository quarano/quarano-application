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

}
