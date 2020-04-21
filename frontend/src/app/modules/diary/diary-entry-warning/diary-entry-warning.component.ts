import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-diary-entry-warning',
  templateUrl: './diary-entry-warning.component.html',
  styleUrls: ['./diary-entry-warning.component.scss']
})
export class DiaryEntryWarningComponent implements OnInit {
  @Input() label: string;
  constructor() { }

  ngOnInit() {
  }

}
