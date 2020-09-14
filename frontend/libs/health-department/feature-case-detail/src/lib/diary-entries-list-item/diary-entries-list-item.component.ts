import { Component, Input, OnInit } from '@angular/core';

export interface TrackedCaseDiaryEntry {
  date: string;
  morning: TrackedCaseDiaryEntrySlot;
  evening: TrackedCaseDiaryEntrySlot;
}

interface TrackedCaseDiaryEntrySlot {
  bodyTemperature: number;
  symptoms: TrackedCaseDiaryEntrySymptom[];
  contacts: TrackedCaseDiaryEntryContact[];
}

interface TrackedCaseDiaryEntrySymptom {
  name: string;
}

interface TrackedCaseDiaryEntryContact {
  firstName: string;
  lastName: string;
}

@Component({
  selector: 'qro-diary-entries-list-item',
  templateUrl: './diary-entries-list-item.component.html',
  styleUrls: ['./diary-entries-list-item.component.scss'],
})
export class DiaryEntriesListItemComponent implements OnInit {
  @Input() diaryEntry: TrackedCaseDiaryEntry;

  constructor() {}

  ngOnInit() {}

  getSymptomsString(diaryEntrySlot: TrackedCaseDiaryEntrySlot): string {
    return diaryEntrySlot.symptoms.map((s) => s.name).join(', ');
  }

  getContactsString(diaryEntrySlot: TrackedCaseDiaryEntrySlot): string {
    return diaryEntrySlot.contacts.map((s) => `${s.firstName} ${s.lastName}`).join(', ');
  }
}
