import { SnackbarService } from '@qro/shared/util-snackbar';
import { HttpResponse } from '@angular/common/http';
import { HealthDepartmentService } from '@qro/health-department/domain';
import { CaseType } from '@qro/auth/api';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ValidationErrorService, DateOrderMatcher, DateOrderValidator } from '@qro/shared/util-forms';
import { Component, OnInit } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { finalize, map, tap } from 'rxjs/operators';
import * as fileSaver from 'file-saver';
import { Moment } from 'moment';
import { BadRequestService } from '@qro/shared/ui-error';

@Component({
  selector: 'qro-export',
  templateUrl: './export.component.html',
  styleUrls: ['./export.component.scss'],
})
export class ExportComponent implements OnInit {
  formGroup: FormGroup;
  CaseType = CaseType;
  loading = false;
  result$: Observable<string>;
  dateOrderMatcher = new DateOrderMatcher();

  constructor(
    public validationErrorService: ValidationErrorService,
    private formBuilder: FormBuilder,
    private healthDepartmentService: HealthDepartmentService,
    private snackbar: SnackbarService,
    private badRequestService: BadRequestService
  ) {}

  ngOnInit() {
    this.createForm();
  }

  private createForm() {
    this.formGroup = this.formBuilder.group(
      {
        selectedExportFormat: new FormControl(null, Validators.required),
        includeOriginCase: new FormControl(false),
        from: new FormControl({ value: moment(), disabled: true }, [Validators.required]),
        to: new FormControl({ value: moment(), disabled: true }, [Validators.required]),
        type: new FormControl(null),
      },
      {
        validators: [DateOrderValidator],
      }
    );
  }

  onTimeOptionChange(event: MatRadioChange) {
    switch (event.value) {
      case '1':
        this.formGroup.controls.from.setValue(moment());
        this.formGroup.controls.from.disable();

        this.formGroup.controls.to.setValue(moment());
        this.formGroup.controls.to.disable();
        break;

      case '2':
        this.formGroup.controls.from.setValue(moment().subtract(1, 'days'));
        this.formGroup.controls.from.disable();

        this.formGroup.controls.to.setValue(moment().subtract(1, 'days'));
        this.formGroup.controls.to.disable();
        break;

      case '3':
        this.formGroup.controls.from.setValue(null);
        this.formGroup.controls.from.enable();

        this.formGroup.controls.to.setValue(null);
        this.formGroup.controls.to.enable();
        break;

      default:
        break;
    }
  }

  onExportFormatChange(event: MatRadioChange) {
    if (event.value === 'sormas' && !this.formGroup.controls.type.value) {
      this.formGroup.controls.type.setValue(CaseType.Index);
    }
  }

  onSubmit() {
    if (this.formGroup.invalid) {
      return;
    }
    this.loading = true;
    const { from, to, type, selectedExportFormat, includeOriginCase } = this.formGroup.getRawValue();

    this.healthDepartmentService
      .performCsvExport(selectedExportFormat, type, from, to, includeOriginCase)
      .pipe(
        map((result: HttpResponse<string>) => new Blob([result.body], { type: result.headers.get('Content-Type') })),
        tap((blob) => {
          fileSaver.saveAs(
            blob,
            `csvexport_` +
              `${selectedExportFormat}_` +
              `${type || 'alle_faelle'}_` +
              `${(from as Moment).format('DD.MM.YYYY')}-${(to as Moment).format('DD.MM.YYYY')}_` +
              `${moment().format('YYYYMMDDHHmmss')}.csv`
          );
        }),
        finalize(() => (this.loading = false))
      )
      .subscribe(
        (_) => {
          this.snackbar.success('CSV-Export erfolgreich ausgeführt');
        },
        (error) => {
          this.badRequestService.handleBadRequestError(error, this.formGroup);
        }
      );
  }
}
