import { Component, OnInit } from '@angular/core';
import { CaseDto, CaseEntityService, TrackedCaseDiaryEntryDto } from '@qro/health-department/domain';
import * as _ from 'lodash';
import { Dictionary } from '@ngrx/entity';
import { DiaryListItemModel } from '../diary-entries-list-item/diary-entries-list-item.component';
import { ActivatedRoute } from '@angular/router';
import { map, shareReplay, switchMap, tap } from 'rxjs/operators';
import { combineLatest, Observable } from 'rxjs';
import { ApiService } from '@qro/shared/util-data-access';

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
    private apiService: ApiService
  ) {}

  ngOnInit() {
    this.listItems$ = combineLatest([this.route.parent.paramMap, this.entityService.entityMap$]).pipe(
      tap(([params, entityMap]) => (this.caseId = params.get('id'))),
      map(([params, entityMap]) => {
        const id = params.get('id');
        if (id) {
          return entityMap[id];
        }
        return this.entityService.emptyCase;
      }),
      switchMap((caseDto: CaseDto) => this.apiService.getApiCall(caseDto, 'diary')),
      map((response) => {
        return this.mapToListItems(this.mapToTrackedCaseDiaryEntryDtoList(response));
      }),
      shareReplay(1)
    );
  }

  private mapToTrackedCaseDiaryEntryDtoList(diaryEntriesList: any): TrackedCaseDiaryEntryDto[] {
    return diaryEntriesList?._embedded?.trackedCaseDiaryEntrySummaryList?.map((entry) => {
      return {
        bodyTemperature: entry.bodyTemperature,
        timeOfDay: entry.slot.timeOfDay,
        date: entry.slot.date,
        symptoms: entry.symptoms.map((symtom) => symtom.name).join(', '),
        contacts: entry.contacts.map((contact) => this.buildContactName(contact)).join(', '),
      } as TrackedCaseDiaryEntryDto;
    });
  }

  private buildContactName(contact) {
    if (!contact.firstName && !contact.lastName) {
      return 'unbekannte Kontaktperson';
    }
    if (!!contact.firstName && !!contact.lastName) {
      return `${contact.firstName} ${contact.lastName}`;
    }
    return !!contact.firstName ? `${contact.firstName} ?` : `? ${contact.lastName}`;
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
      const dateA = Date.parse(entryA.date);
      const dateB = Date.parse(entryB.date);
      if (dateA > dateB) return -1;
      if (dateA < dateB) return 1;
      return 0;
    });
  }
}
