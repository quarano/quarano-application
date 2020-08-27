import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'qro-translated-confirmation-dialog',
  templateUrl: './translated-confirmation-dialog.component.html',
  styleUrls: ['./translated-confirmation-dialog.component.scss'],
})
export class TranslatedConfirmationDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: {
      text: string;
      title: string;
      confirmButtonText: string;
      abortButtonText: string;
    },
    private matDialogRef: MatDialogRef<TranslatedConfirmationDialogComponent>
  ) {}

  public close() {
    this.matDialogRef.close();
  }
}
