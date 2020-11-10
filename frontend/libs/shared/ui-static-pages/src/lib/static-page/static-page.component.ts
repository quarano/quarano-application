import { Observable } from 'rxjs';
import { StaticPageKeys } from './../model/static-page';
import { Component, Input, OnInit } from '@angular/core';
import { StaticPageState } from '../reducers';
import { select, Store } from '@ngrx/store';
import { StaticPageSelectors } from '../store/selector-types';
import { map } from 'rxjs/operators';

@Component({
  selector: 'qro-static-page',
  templateUrl: './static-page.component.html',
  styleUrls: ['./static-page.component.scss'],
})
export class StaticPageComponent implements OnInit {
  @Input() staticPageKey: StaticPageKeys;

  htmlContent$: Observable<string>;

  constructor(private store: Store<StaticPageState>) {}

  ngOnInit() {
    this.htmlContent$ = this.store.pipe(
      select(StaticPageSelectors.selectStaticPageByKey, { key: this.staticPageKey }),
      map((dto) => dto.text)
    );
  }
}
