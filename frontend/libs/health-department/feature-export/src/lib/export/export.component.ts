import { HealthDepartmentService } from '@qro/health-department/domain';
import { CaseType } from '@qro/auth/api';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ValidationErrorService, DateOrderMatcher, DateOrderValidator } from '@qro/shared/util-forms';
import { Component, OnInit } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

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
    private healthDepartmentService: HealthDepartmentService
  ) {}

  ngOnInit() {
    this.createForm();
  }

  private createForm() {
    this.formGroup = this.formBuilder.group(
      {
        start: new FormControl({ value: moment(), disabled: true }, [Validators.required]),
        end: new FormControl({ value: moment(), disabled: true }, [Validators.required]),
        caseType: new FormControl(null),
      },
      {
        validators: [DateOrderValidator],
      }
    );
  }

  onTimeOptionChange(event: MatRadioChange) {
    switch (event.value) {
      case '1':
        this.formGroup.controls.start.setValue(moment());
        this.formGroup.controls.start.disable();

        this.formGroup.controls.end.setValue(moment());
        this.formGroup.controls.end.disable();
        break;

      case '2':
        this.formGroup.controls.start.setValue(moment().subtract(1, 'days'));
        this.formGroup.controls.start.disable();

        this.formGroup.controls.end.setValue(moment().subtract(1, 'days'));
        this.formGroup.controls.end.disable();
        break;

      case '3':
        this.formGroup.controls.start.setValue(null);
        this.formGroup.controls.start.enable();

        this.formGroup.controls.end.setValue(null);
        this.formGroup.controls.end.enable();
        break;

      default:
        break;
    }
  }

  onSubmit() {
    if (this.formGroup.invalid) {
      return;
    }
    this.loading = true;
    const { start, end, caseType } = this.formGroup.getRawValue();
    this.result$ = this.healthDepartmentService
      .getCsvData(caseType, start, end)
      .pipe(finalize(() => (this.loading = false)));
  }
}
