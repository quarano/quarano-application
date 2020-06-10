import { Component, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Subject } from 'rxjs';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { StartTracking } from '@qro/health-department/domain';

@Component({
  selector: 'qro-client-mail',
  templateUrl: './mail.component.html',
  styleUrls: ['./mail.component.scss'],
})
export class MailComponent implements OnInit, OnChanges {
  @Input()
  tracking: StartTracking;

  mailText: SafeHtml;

  @Output()
  renewTrackingCode: Subject<void> = new Subject<void>();

  constructor(private domSanitizer: DomSanitizer, private snackbarService: SnackbarService) {}

  ngOnInit(): void {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.hasOwnProperty('tracking')) {
      this.mailText = this.domSanitizer.bypassSecurityTrustHtml(this.tracking.email);
    }
  }

  renewTrackickng() {
    this.renewTrackingCode.next();
    this.snackbarService.success('Der Aktivierungscode wurde erneuert.');
  }

  copyToClipBoard() {
    this.snackbarService.success('Der E-Mail Text wurde in die Zwischenablage kopiert.');
    navigator.clipboard.writeText(this.tracking.email);
  }
}
