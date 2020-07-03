import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'qro-data-protection-dialog',
  templateUrl: './data-protection-dialog.component.html',
  styleUrls: ['./data-protection-dialog.component.scss'],
})
export class DataProtectionDialogComponent implements OnInit {
  constructor(private matDialogRef: MatDialogRef<DataProtectionDialogComponent>) {}

  ngOnInit() {}

  public close() {
    this.matDialogRef.close();
  }
}
