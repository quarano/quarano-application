import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'qro-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss']
})
export class AlertComponent implements OnInit {
  @Input() message: string;

  constructor() { }

  ngOnInit() {
  }

}
