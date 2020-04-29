import {Component, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {StartTracking} from '@models/start-tracking';
import {DomSanitizer, SafeHtml} from '@angular/platform-browser';
import {Subject} from 'rxjs';

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

  constructor(private domSanitizer: DomSanitizer) {
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
  }

  copyToClipBoard() {
    navigator.clipboard.writeText(this.tracking.email);
  }

}
