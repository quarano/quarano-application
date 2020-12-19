import { RouterTestingModule } from '@angular/router/testing';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { CaseListComponent } from './case-list.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { CaseEntityService, HealthDepartmentService } from '@qro/health-department/domain';
import { BadRequestService } from '@qro/shared/ui-error';
import { SnackbarService } from '@qro/shared/util-snackbar';

describe('CaseListComponent', () => {
  let component: CaseListComponent;
  let fixture: ComponentFixture<CaseListComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [RouterTestingModule],
        declarations: [CaseListComponent],
        schemas: [NO_ERRORS_SCHEMA],
        providers: [
          { provide: ActivatedRoute, useValue: { data: of({ cases: [] }) } },
          { provide: CaseEntityService, useValue: {} },
          { provide: HealthDepartmentService, useValue: {} },
          { provide: BadRequestService, useValue: {} },
          { provide: SnackbarService, useValue: {} },
        ],
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(CaseListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
