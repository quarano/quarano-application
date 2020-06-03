import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseDetailComponent } from './case-detail.component';
import { RouterTestingModule } from '@angular/router/testing';
import { MatDialog } from '@angular/material/dialog';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { SnackbarService } from '@qro/shared/util';
import { ApiService } from '@qro/shared/util-data-access';
import { HealthDepartmentService } from '@qro/health-department/domain';

describe('CaseDetailComponent', () => {
  let component: CaseDetailComponent;
  let fixture: ComponentFixture<CaseDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [CaseDetailComponent],
      providers: [
        { provide: MatDialog, useValue: {} },
        { provide: ApiService, useValue: {} },
        { provide: SnackbarService, useValue: {} },
        { provide: HealthDepartmentService, useValue: {} },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CaseDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
