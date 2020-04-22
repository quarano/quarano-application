import { DiaryListItemDto, DiaryEntryListItemDto } from '@models/diary-entry';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-diary-list-item',
  templateUrl: './diary-list-item.component.html',
  styleUrls: ['./diary-list-item.component.scss']
})
export class DiaryListItemComponent {
  @Input() diaryListItem: DiaryListItemDto;
  @Input() isFirst: boolean;

  get morning(): DiaryEntryListItemDto {
    console.log(this.diaryListItem.morning);
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
