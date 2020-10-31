import { Component, OnInit } from '@angular/core';
import {
  CaseDto,
  CaseEntityService,
  HealthDepartmentService,
  TrackedCaseDiaryEntryDto,
} from '@qro/health-department/domain';
import * as _ from 'lodash';
import { Dictionary } from '@ngrx/entity';
import { DiaryListItemModel } from '../diary-entries-list-item/diary-entries-list-item.component';
import { ActivatedRoute } from '@angular/router';
import { map, shareReplay, switchMap, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Component({
  selector: 'qro-diary-entries-list',
  templateUrl: './diary-entries-list.component.html',
  styleUrls: ['./diary-entries-list.component.scss'],
})
export class DiaryEntriesListComponent implements OnInit {
  listItems$: Observable<DiaryListItemModel[]>;
  private caseId: string;

  private readonly MORNING = 'morning';
  private readonly EVENING = 'evening';

  constructor(
    private route: ActivatedRoute,
    private entityService: CaseEntityService,
    private healthDepartmentService: HealthDepartmentService
  ) {}

  ngOnInit() {
    this.listItems$ = this.route.parent.paramMap.pipe(
      tap((params) => (this.caseId = params.get('id'))),
      switchMap((params) => this.entityService.loadOneFromStore(params.get('id'))),
      switchMap((caseDto: CaseDto) => this.healthDepartmentService.getCaseDiaryEntries(caseDto.caseId)), // HAL service aus shared data access, Schwellenwert, self-link in Antwort
      map((diaryEntries: TrackedCaseDiaryEntryDto[]) => {
        return this.mapToListItems(diaryEntries);
      }),
      shareReplay(1)
    );
  }

  private mapToListItems(diaryEntriesDto: TrackedCaseDiaryEntryDto[]) {
    if (!!diaryEntriesDto && diaryEntriesDto.length > 0) {
      const groupedDiaryEntryDto = this.groupEntriesByDate(diaryEntriesDto);

      const convertedListItems = this.convertToListItems(groupedDiaryEntryDto);

      return this.sortByDateDescending(convertedListItems);
    }
  }

  private convertToListItems(groupedDiaryEntries: Dictionary<any[]>): DiaryListItemModel[] {
    const convertedItems: DiaryListItemModel[] = [];

    for (const [date, diaryEntries] of Object.entries(groupedDiaryEntries)) {
      const entryMorning = diaryEntries.filter(
        (diaryEntry: TrackedCaseDiaryEntryDto) => diaryEntry.timeOfDay === this.MORNING
      )?.[0];
      const entryEvening = diaryEntries.filter(
        (diaryEntry: TrackedCaseDiaryEntryDto) => diaryEntry.timeOfDay === this.EVENING
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
      return entry.date;
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
