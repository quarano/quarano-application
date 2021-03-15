import { LocationDto } from '@qro/client/domain';
import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'qro-location-dialog',
  templateUrl: './location-dialog.component.html',
  styleUrls: ['./location-dialog.component.scss'],
})
export class LocationDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: {
      location: LocationDto;
    },
    private matDialogRef: MatDialogRef<LocationDialogComponent>
  ) {}

  public close() {
    this.matDialogRef.close();
  }

  onLocationCreated(createdLocation: LocationDto) {
    this.matDialogRef.close(createdLocation);
  }
}
