import { Component, OnInit, Input } from '@angular/core';
import { DiaryEntryListItemDto } from '@qro/client/diary/domain';

@Component({
  selector: 'qro-diary-entry-success',
  templateUrl: './diary-entry-success.component.html',
  styleUrls: ['./diary-entry-success.component.scss'],
})
export class DiaryEntrySuccessComponent implements OnInit {
  @Input() entry: DiaryEntryListItemDto;
  @Input() label: string;

  constructor() {}

  ngOnInit() {}

  get isReadonly(): boolean {
    return !this.entry._links.edit;
  }

  get symptomsString(): string {
    return this.entry.symptoms.map((s) => s.name).join(', ');
  }

  get contactsString(): string {
    return this.entry.contacts.map((s) => `${s.firstName} ${s.lastName}`).join(', ');
  }
}
