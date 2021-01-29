import { Component, Input } from '@angular/core';
import { OccasionDetailDialogComponent } from '../occasion-detail-dialog/occasion-detail-dialog.component';
import { filter, switchMap, take } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { SubSink } from 'subsink';
import { OccasionDto } from '../../../../../domain/src/lib/model/occasion';
import { OccasionService } from '../occasion.service';

@Component({
  selector: 'qro-occasion-card',
  templateUrl: './occasion-card.component.html',
  styleUrls: ['./occasion-card.component.scss'],
})
export class OccasionCardComponent {
  @Input()
  occasion: OccasionDto;

  subs = new SubSink();

  constructor(private occasionService: OccasionService, private dialog: MatDialog) {}

  deleteOccasion() {
    this.occasionService.deleteOccasion(this.occasion).subscribe((value) => console.log(value));
  }

  editOccasion(occasion: OccasionDto) {
    const dialogRef = this.dialog.open(OccasionDetailDialogComponent);
    dialogRef.componentInstance.occasion(occasion);
    dialogRef
      .afterClosed()
      .pipe(
        take(1),
        filter((occasionData) => occasionData),
        switchMap((occasionData) => this.occasionService.editOccasion(occasionData.occasionCode, occasionData))
      )
      .subscribe((response) => {
        console.log('edit-backend-response:', response);
      });
  }
}
