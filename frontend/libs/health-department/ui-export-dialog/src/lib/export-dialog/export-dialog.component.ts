import { HealthDepartmentService } from '@qro/health-department/domain';
import { CaseType } from '@qro/auth/api';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { finalize, map, tap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import * as fileSaver from 'file-saver';
import * as moment from 'moment';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { BadRequestService } from '@qro/shared/ui-error';

@Component({
  selector: 'app-export-dialog',
  templateUrl: './export-dialog.component.html',
  styleUrls: ['./export-dialog.component.scss'],
})
export class ExportDialogComponent implements OnInit {
  public loading = false;
  public selectedExportFormat: string;
  public includeOriginCase: boolean = false;

  constructor(
    private matDialogRef: MatDialogRef<ExportDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { idList: string[]; caseType: CaseType },
    private healthDepartmentService: HealthDepartmentService,
    private snackbar: SnackbarService,
    private badRequestService: BadRequestService
  ) {}

  ngOnInit() {}

  cancel() {
    this.matDialogRef.close();
  }

  exportNow() {
    this.loading = true;
    console.log(this.data.idList);
    let url = `${this.selectedExportFormat}/by-ids`;
    if (this.includeOriginCase) {
      url += '?withorigincase=true';
    }
    this.performExport(url);
  }

  private performExport(url: string) {
    this.healthDepartmentService
      .performCsvExport(url, this.data.idList)
      .pipe(
        map((result: HttpResponse<string>) => new Blob([result.body], { type: result.headers.get('Content-Type') })),
        tap((blob) => {
          fileSaver.saveAs(
            blob,
            `csvexport_${this.selectedExportFormat}_${this.data.caseType}_gefiltert_${moment().format(
              'YYYYMMDDHHmmss'
            )}.csv`
          );
        }),
        finalize(() => (this.loading = false))
      )
      .subscribe(
        (_) => {
          this.snackbar.success('CSV-Export erfolgreich ausgeführt');
          this.matDialogRef.close();
        },
        (error) => {
          this.badRequestService.handleBadRequestError(error, null);
        }
      );
  }
}
