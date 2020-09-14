import { Component, Input, OnInit } from '@angular/core';
import { TrackedCaseDiaryEntryDto } from '@qro/health-department/domain';
import * as _ from 'lodash';
import { TrackedCaseDiaryEntry } from '../diary-entries-list-item/diary-entries-list-item.component';
import { Dictionary } from '@ngrx/entity';

@Component({
  selector: 'qro-diary-entries-list',
  templateUrl: './diary-entries-list.component.html',
  styleUrls: ['./diary-entries-list.component.scss'],
})
export class DiaryEntriesListComponent implements OnInit {
  @Input() entriesDto: TrackedCaseDiaryEntryDto[];

  diaryEntries: TrackedCaseDiaryEntry[] = [];

  private readonly MORNING = 'morning';
  private readonly EVENING = 'evening';

  constructor() {}

  ngOnInit() {
    this.mapToEntries(this.entriesDto);
  }

  private mapToEntries(diaryEntriesDto: TrackedCaseDiaryEntryDto[]) {
    if (!!diaryEntriesDto && diaryEntriesDto.length > 0) {
      const groupedDiaryEntryDto = this.groupEntriesByDate(diaryEntriesDto);

      const convertedEntries = this.convertToDiaryEntries(groupedDiaryEntryDto);

      this.diaryEntries = this.sortByDateDescending(convertedEntries);
    }
  }

  private convertToDiaryEntries(groupedDiaryEntries: Dictionary<any[]>): TrackedCaseDiaryEntry[] {
    const mappedEntries: TrackedCaseDiaryEntry[] = [];
    for (const [date, diaryEntries] of Object.entries(groupedDiaryEntries)) {
      const entryMorning = diaryEntries.filter(
        (diaryEntry: TrackedCaseDiaryEntryDto) => diaryEntry.slot.timeOfDay === this.MORNING
      )?.[0];
      const entryEvening = diaryEntries.filter(
        (diaryEntry: TrackedCaseDiaryEntryDto) => diaryEntry.slot.timeOfDay === this.EVENING
      )?.[0];

      const entry = {
        date: date,
        morning: entryMorning,
        evening: entryEvening,
      } as TrackedCaseDiaryEntry;

      mappedEntries.push(entry);
    }
    return mappedEntries;
  }

  private groupEntriesByDate(diaryEntriesDto: TrackedCaseDiaryEntryDto[]) {
    return _.groupBy(diaryEntriesDto, (entry) => {
      return entry.slot.date;
    });
  }

  private sortByDateDescending(convertedEntries: TrackedCaseDiaryEntry[]) {
    return convertedEntries.sort((entryA, entryB) => {
      if (entryA.date < entryB.date) return -1;
      if (entryA.date < entryB.date) return 1;
      return 0;
    });
  }
}
