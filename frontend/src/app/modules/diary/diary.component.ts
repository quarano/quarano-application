import { AsideService } from '@services/aside.service';
import { DiaryEntryDto } from '@models/diary-entry';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';
import { KeyValue } from '@angular/common';
import { ForgottenContactBannerComponent } from './forgotten-contact-banner/forgotten-contact-banner.component';

@Component({
  selector: 'app-diary',
  templateUrl: './diary.component.html',
  styleUrls: ['./diary.component.scss']
})
export class DiaryComponent implements OnInit, OnDestroy {
  diaryEntries: Map<string, DiaryEntryDto[]> = new Map<string, DiaryEntryDto[]>();
  private subs = new SubSink();
  today = new Date().toLocaleDateString('de');
  displayedColumns = ['dateTime', 'bodyTemperature', 'symptoms', 'contactPersonList', 'transmittedToHealthDepartment'];

  constructor(
    private route: ActivatedRoute,
    private asideService: AsideService) { }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.diaryEntries = data.diaryEntries;
    }));
    this.asideService.setAsideComponentContent(ForgottenContactBannerComponent);
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
    this.asideService.clearAsideComponentContent();
  }

  originalOrder = (a: KeyValue<string, DiaryEntryDto[]>, b: KeyValue<string, DiaryEntryDto[]>): number => {
    return 0;
  }
}
