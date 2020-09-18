import { Component, Input, OnInit } from '@angular/core';
import { TrackedCaseDiaryEntryDto } from '@qro/health-department/domain';
import * as _ from 'lodash';
import { Dictionary } from '@ngrx/entity';
import { DiaryListItemModel } from '../diary-entries-list-item/diary-entries-list-item.component';

@Component({
  selector: 'qro-diary-entries-list',
  templateUrl: './diary-entries-list.component.html',
  styleUrls: ['./diary-entries-list.component.scss'],
})
export class DiaryEntriesListComponent implements OnInit {
  @Input() entriesDto: TrackedCaseDiaryEntryDto[];

  listItems: DiaryListItemModel[] = [];

  private readonly MORNING = 'morning';
  private readonly EVENING = 'evening';

  constructor() {}

  ngOnInit() {
    this.mapToListItems(this.entriesDto);
  }

  private mapToListItems(diaryEntriesDto: TrackedCaseDiaryEntryDto[]) {
    if (!!diaryEntriesDto && diaryEntriesDto.length > 0) {
      const groupedDiaryEntryDto = this.groupEntriesByDate(diaryEntriesDto);

      const convertedListItems = this.convertToListItems(groupedDiaryEntryDto);

      this.listItems = this.sortByDateDescending(convertedListItems);
    }
  }

  private convertToListItems(groupedDiaryEntries: Dictionary<any[]>): DiaryListItemModel[] {
    const convertedItems: DiaryListItemModel[] = [];
    for (const [date, diaryEntries] of Object.entries(groupedDiaryEntries)) {
      const entryMorning = diaryEntries.filter(
        (diaryEntry: TrackedCaseDiaryEntryDto) => diaryEntry.slot.timeOfDay === this.MORNING
      )?.[0];
      const entryEvening = diaryEntries.filter(
        (diaryEntry: TrackedCaseDiaryEntryDto) => diaryEntry.slot.timeOfDay === this.EVENING
      )?.[0];

      const item = {
        date: date,
        morning: entryMorning,
        evening: entryEvening,
      } as DiaryListItemModel;

      convertedItems.push(item);
    }
    return convertedItems;
  }

  private groupEntriesByDate(diaryEntriesDto: TrackedCaseDiaryEntryDto[]) {
    return _.groupBy(diaryEntriesDto, (entry) => {
      return entry.slot.date;
    });
  }

  private sortByDateDescending(items: DiaryListItemModel[]) {
    return items.sort((entryA, entryB) => {
      if (entryA.date < entryB.date) return -1;
      if (entryA.date < entryB.date) return 1;
      return 0;
    });
  }
}
