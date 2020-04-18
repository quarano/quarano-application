import { DiaryEntryDto } from '@models/diary-entry';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-diary-list-item',
  templateUrl: './diary-list-item.component.html',
  styleUrls: ['./diary-list-item.component.scss']
})
export class DiaryListItemComponent {
  @Input() entries: DiaryEntryDto[];
  @Input() date: string;

  toStringSymptoms(entry: DiaryEntryDto): string {
    return entry.symptoms.map(s => s.name).join(', ');
  }

  toStringContactPersons(entry: DiaryEntryDto): string {
    return entry.contacts.map(s => `${s.firstName} ${s.lastName}`).join(', ');
  }
}
