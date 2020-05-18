import { Component, Input } from '@angular/core';
import {DiaryEntryListItemDto, DiaryListItemDto} from '../../../models/diary-entry';

@Component({
  selector: 'qro-diary-list-item',
  templateUrl: './diary-list-item.component.html',
  styleUrls: ['./diary-list-item.component.scss']
})
export class DiaryListItemComponent {
  @Input() diaryListItem: DiaryListItemDto;

  get morning(): DiaryEntryListItemDto {
    if (this.diaryListItem.morning.hasOwnProperty('id')) {
      return this.diaryListItem.morning as DiaryEntryListItemDto;
    }
    return null;
  }

  get evening(): DiaryEntryListItemDto {
    if (this.diaryListItem.evening.hasOwnProperty('id')) {
      return this.diaryListItem.evening as DiaryEntryListItemDto;
    }
    return null;
  }

  get canCreateMorning(): boolean {
    return this.diaryListItem.morning.hasOwnProperty('_links');
  }

  get canCreateEvening(): boolean {
    return this.diaryListItem.evening.hasOwnProperty('_links');
  }

  get date(): Date {
    return new Date(this.diaryListItem.date);
  }

}
