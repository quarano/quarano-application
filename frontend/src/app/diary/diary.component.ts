import { DiaryEntryDto } from 'src/app/models/diary-entry';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';
import { KeyValue } from '@angular/common';

@Component({
  selector: 'app-diary',
  templateUrl: './diary.component.html',
  styleUrls: ['./diary.component.scss']
})
export class DiaryComponent implements OnInit, OnDestroy {
  diaryEntries: Map<string, DiaryEntryDto[]> = new Map<string, DiaryEntryDto[]>();
  private subs = new SubSink();
  today = new Date().toLocaleDateString();
  displayedColumns = ['dateTime', 'bodyTemperature', 'symptoms', 'contactPersonList', 'transmittedToHealthDepartment'];

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.diaryEntries = data.diaryEntries;
    }));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  originalOrder = (a: KeyValue<string, DiaryEntryDto[]>, b: KeyValue<string, DiaryEntryDto[]>): number => {
    return 0;
  }
}
