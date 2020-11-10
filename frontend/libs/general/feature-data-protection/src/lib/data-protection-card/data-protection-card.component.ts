import { Component, OnInit } from '@angular/core';
import { StaticPageKeys } from '@qro/shared/ui-static-pages';

@Component({
  selector: 'qro-data-protection-card',
  templateUrl: './data-protection-card.component.html',
  styleUrls: ['./data-protection-card.component.scss'],
})
export class DataProtectionCardComponent implements OnInit {
  StaticPageKeys = StaticPageKeys;
  constructor() {}

  ngOnInit() {}
}
