import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormControl, FormGroup} from '@angular/forms';

export interface CloseCaseDialogResponse {
  comment: any;
  confirmation: boolean;
}

@Component({
  selector: 'app-close-case-dialog',
  templateUrl: './close-case-dialog.component.html',
  styleUrls: ['./close-case-dialog.component.scss']
})
export class CloseCaseDialogComponent {

  commentGroup = new FormGroup({
    comment: new FormControl()
  });

  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: {
      text: string;
      title: string;
    },
    private matDialogRef: MatDialogRef<CloseCaseDialogComponent>
  ) {
  }

  public close() {
    const response: CloseCaseDialogResponse = {
      comment: this.commentGroup.get('comment').value,
      confirmation: false
    };
    this.matDialogRef.close(response);
  }

  public confirm() {
    const response: CloseCaseDialogResponse = {
      comment: this.commentGroup.get('comment').value,
      confirmation: true
    };
    this.matDialogRef.close(response);
  }

}
