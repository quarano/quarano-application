import { Component, Input, OnInit } from '@angular/core';

export interface DiaryListItemModel {
  date: string;
  morning: CellModel;
  evening: CellModel;
}

interface CellModel {
  bodyTemperature: number;
  symptoms: string[];
  contacts: string[];
}

@Component({
  selector: 'qro-diary-entries-list-item',
  templateUrl: './diary-entries-list-item.component.html',
  styleUrls: ['./diary-entries-list-item.component.scss'],
})
export class DiaryEntriesListItemComponent implements OnInit {
  @Input() diaryListItem: DiaryListItemModel;

  constructor() {}

  ngOnInit() {}

  format(theArray: string[]) {
    return theArray.join(', ');
  }
}
