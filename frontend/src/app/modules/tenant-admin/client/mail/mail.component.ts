import {Component, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {StartTracking} from '@models/start-tracking';
import {DomSanitizer, SafeHtml} from '@angular/platform-browser';
import {Subject} from 'rxjs';
import {ApiService} from '@services/api.service';
import {SnackbarService} from '@services/snackbar.service';

@Component({
  selector: 'app-client-mail',
  templateUrl: './mail.component.html',
  styleUrls: ['./mail.component.scss']
})
export class MailComponent implements OnInit, OnChanges {

  @Input()
  tracking: StartTracking;

  mailText: SafeHtml;


  @Output()
  renewTrackingCode$$: Subject<void> = new Subject<void>();

  constructor(private domSanitizer: DomSanitizer,
              private snackbarService: SnackbarService) {
  }

  ngOnInit(): void {

  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.hasOwnProperty('tracking')) {
      this.mailText = this.domSanitizer.bypassSecurityTrustHtml(this.tracking.email);
    }
  }


  renewTrackickng() {
    this.renewTrackingCode$$.next();
    this.snackbarService.success('Der Aktivierungscode wurde erneuert.');

  }

  copyToClipBoard() {
    this.snackbarService.success('Der E-Mail Text wurde in die Zwischenablage kopiert.');
    navigator.clipboard.writeText(this.tracking.email);
  }

}
