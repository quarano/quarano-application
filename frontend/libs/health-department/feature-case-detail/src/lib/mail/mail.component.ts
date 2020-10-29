import { ActivatedRoute } from '@angular/router';
import { ApiService, HalResponse } from '@qro/shared/util-data-access';
import { Component, OnInit, Output } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Subject, Observable, of, combineLatest } from 'rxjs';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { StartTracking, CaseEntityService } from '@qro/health-department/domain';
import { map, switchMap, tap, shareReplay } from 'rxjs/operators';

@Component({
  selector: 'qro-client-mail',
  templateUrl: './mail.component.html',
  styleUrls: ['./mail.component.scss'],
})
export class MailComponent implements OnInit {
  tracking$: Observable<StartTracking>;

  mailText$: Observable<SafeHtml>;

  @Output()
  renewTrackingCode: Subject<void> = new Subject<void>();

  constructor(
    private domSanitizer: DomSanitizer,
    private snackbarService: SnackbarService,
    private apiService: ApiService,
    private route: ActivatedRoute,
    private entityService: CaseEntityService
  ) {}

  ngOnInit(): void {
    this.tracking$ = combineLatest([
      this.route.parent.paramMap.pipe(map((paramMap) => paramMap.get('id'))),
      this.entityService.entityMap$,
    ]).pipe(
      map(([id, entityMap]) => {
        return entityMap[id];
      }),
      shareReplay(1),
      switchMap((data) => {
        if (data?._links?.hasOwnProperty('renew')) {
          return this.apiService.getApiCall<StartTracking>(data, 'renew');
        }
        return of(null);
      })
    );

    this.mailText$ = this.tracking$.pipe(map((tracking) => this.domSanitizer.bypassSecurityTrustHtml(tracking?.email)));
  }

  renewTracking(tracking: HalResponse) {
    this.tracking$ = this.apiService
      .putApiCall<StartTracking>(tracking, 'renew')
      .pipe(tap((track) => this.snackbarService.success('Der Aktivierungscode wurde erneuert.')));
  }

  copyToClipBoard(tracking: StartTracking) {
    this.snackbarService.success('Der E-Mail Text wurde in die Zwischenablage kopiert.');
    navigator.clipboard.writeText(tracking.email);
  }
}
