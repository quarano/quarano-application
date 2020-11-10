import { TranslateService } from '@ngx-translate/core';
import { switchMap } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { of, Observable } from 'rxjs';

@Component({
  selector: 'qro-not-found',
  templateUrl: './not-found.component.html',
  styleUrls: ['./not-found.component.scss'],
})
export class NotFoundComponent implements OnInit {
  message$: Observable<string>;

  constructor(private activatedRoute: ActivatedRoute, private translate: TranslateService) {}

  ngOnInit(): void {
    this.message$ = this.activatedRoute.params.pipe(
      switchMap((params) => {
        if (params.message) {
          return of(params.message);
        }
        return this.translate.get('NOT_FOUND.DEFAULT_MESSAGE');
      })
    );
  }
}
