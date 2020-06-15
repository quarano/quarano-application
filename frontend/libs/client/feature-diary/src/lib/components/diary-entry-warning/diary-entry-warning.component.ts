import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'qro-diary-entry-warning',
  templateUrl: './diary-entry-warning.component.html',
  styleUrls: ['./diary-entry-warning.component.scss'],
})
export class DiaryEntryWarningComponent implements OnInit {
  @Input() slot: 'morning' | 'evening';
  @Input() date: string;
  @Input() canCreate: boolean;
  constructor() {}

  ngOnInit() {}
}
