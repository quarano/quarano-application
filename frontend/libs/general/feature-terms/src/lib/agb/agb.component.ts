import { StaticPageKeys } from '@qro/shared/ui-static-pages';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'qro-agb',
  templateUrl: './agb.component.html',
  styleUrls: ['./agb.component.scss'],
})
export class AgbComponent implements OnInit {
  StaticPageKeys = StaticPageKeys;

  constructor() {}

  ngOnInit() {}
}
