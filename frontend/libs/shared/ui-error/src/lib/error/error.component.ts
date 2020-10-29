import { Observable } from 'rxjs';
import { SubSink } from 'subsink';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { map } from 'rxjs/operators';

@Component({
  selector: 'qro-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss'],
})
export class ErrorComponent implements OnInit, OnDestroy {
  private subs = new SubSink();
  message$: Observable<string>;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.message$ = this.route.queryParamMap.pipe(
      map((paramMap) => paramMap.get('message') || 'ERROR.UNBEKANNTER_FEHLER')
    );
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}
