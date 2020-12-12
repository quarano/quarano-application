import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'qro-event-card',
  templateUrl: './event-card.component.html',
  styleUrls: ['./event-card.component.scss'],
})
export class EventCardComponent implements OnInit {
  @Input()
  occasion: any;

  constructor() {}

  ngOnInit(): void {}
}
