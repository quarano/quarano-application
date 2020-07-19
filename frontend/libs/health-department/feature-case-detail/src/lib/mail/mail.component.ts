import { ActivatedRoute } from '@angular/router';
import { ApiService, HalResponse } from '@qro/shared/util-data-access';
import { Component, OnInit, Output } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Subject, Observable, of } from 'rxjs';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { StartTracking } from '@qro/health-department/domain';
import { map, switchMap, tap } from 'rxjs/operators';

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
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.tracking$ = this.route.parent.data.pipe(
      switchMap((data) => {
        if (data.case?._links?.hasOwnProperty('renew')) {
          return this.apiService.getApiCall<StartTracking>(data.case, 'renew');
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
