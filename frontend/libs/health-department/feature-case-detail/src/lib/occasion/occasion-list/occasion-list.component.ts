import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SubSink } from 'subsink';
import { filter, take, tap } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { OccasionDetailDialogComponent } from '../occasion-detail-dialog/occasion-detail-dialog.component';
import { Observable } from 'rxjs';
import { OccasionDto } from '../../../../../domain/src/lib/model/occasion';
import { OccasionService } from '../occasion.service';

@Component({
  selector: 'qro-occasion-list',
  templateUrl: './occasion-list.component.html',
  styleUrls: ['./occasion-list.component.scss'],
})
export class OccasionListComponent implements OnInit, OnDestroy {
  subs = new SubSink();
  caseId = null;

  $occasions: Observable<OccasionDto[]>;

  constructor(private dialog: MatDialog, private route: ActivatedRoute, private occasionService: OccasionService) {
    this.route.parent.paramMap
      .pipe(
        take(1),
        tap((params) => (this.caseId = params.get('id')))
      )
      .subscribe();

    this.$occasions = this.occasionService.getOccasions();
  }

  ngOnInit(): void {}

  openOccasionDetailDialog() {
    this.subs.add(
      this.dialog
        .open(OccasionDetailDialogComponent)
        .afterClosed()
        .pipe(filter((occasionData) => occasionData))
        .subscribe((occasionData) => this.saveNewOccasion(occasionData))
    );
  }

  private saveNewOccasion(newOccasion) {
    this.occasionService
      .saveOccasion(this.caseId, newOccasion)
      .pipe(take(1))
      .subscribe((value) => console.log(value));
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
}
