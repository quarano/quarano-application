import { Component, Input, OnInit } from '@angular/core';
import { TrackedCaseDiaryEntryContactDto } from '@qro/health-department/domain';

export interface DiaryListItemModel {
  date: string;
  morning: CellModel;
  evening: CellModel;
}

interface CellModel {
  bodyTemperature: number;
  symptoms: string[];
  contacts: TrackedCaseDiaryEntryContactDto[];
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

  getSymptomsString(cell: CellModel): string {
    return cell.symptoms.join(', ');
  }

  getContactsString(cell: CellModel): string {
    return cell.contacts.map((s) => `${s.firstName} ${s.lastName}`).join(', ');
  }
}
