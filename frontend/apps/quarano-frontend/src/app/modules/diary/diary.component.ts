import {ForgottenContactDialogComponent} from './forgotten-contact-dialog/forgotten-contact-dialog.component';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {SubSink} from 'subsink';
import {ForgottenContactBannerComponent} from './forgotten-contact-banner/forgotten-contact-banner.component';
import {MatDialog} from '@angular/material/dialog';
import {AsideService} from '../../services/aside.service';
import {DiaryDto} from '../../models/diary-entry';
import {ApiService} from '../../services/api.service';

@Component({
  selector: 'qro-diary',
  templateUrl: './diary.component.html',
  styleUrls: ['./diary.component.scss']
})
export class DiaryComponent implements OnInit, OnDestroy {
  diary: DiaryDto;
  private subs = new SubSink();

  constructor(
    private route: ActivatedRoute,
    private asideService: AsideService,
    private dialog: MatDialog,
    private apiService: ApiService) {
  }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.diary = data.diary;
    }));
    this.asideService.setAsideComponentContent(ForgottenContactBannerComponent);
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
    this.asideService.clearAsideComponentContent();
  }

  openContactDialog() {
    this.subs.add(this.apiService.getContactPersons()
      .subscribe(contactPersons => this.dialog.open(ForgottenContactDialogComponent, {
        data: {
          contactPersons
        }
      })));
  }
}
