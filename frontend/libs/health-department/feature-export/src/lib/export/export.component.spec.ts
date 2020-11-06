import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { SharedUiMaterialModule } from '@qro/shared/ui-material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { BadRequestService } from '@qro/shared/ui-error';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { ExportComponent } from './export.component';
import { ValidationErrorService } from '@qro/shared/util-forms';
import { HealthDepartmentService } from '@qro/health-department/domain';

describe('ExportComponent', () => {
  let component: ExportComponent;
  let fixture: ComponentFixture<ExportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ExportComponent],
      schemas: [NO_ERRORS_SCHEMA],
      imports: [FormsModule, ReactiveFormsModule, SharedUiMaterialModule, NoopAnimationsModule],
      providers: [
        { provide: ValidationErrorService, useValue: { getErrorKeys: () => [] } },
        { provide: HealthDepartmentService, useValue: {} },
        { provide: BadRequestService, useValue: {} },
        { provide: SnackbarService, useValue: {} },
      ],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
