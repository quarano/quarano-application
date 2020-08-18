import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActionComponent } from './action.component';
import { MatDialog } from '@angular/material/dialog';
import { FormBuilder } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { HealthDepartmentService } from '@qro/health-department/domain';
import { CaseActionDto } from '@qro/health-department/domain';

describe('ActionComponent', () => {
  let component: ActionComponent;
  let fixture: ComponentFixture<ActionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [ActionComponent],
      providers: [
        FormBuilder,
        { provide: MatDialog, useValue: {} },
        { provide: SnackbarService, useValue: {} },
        { provide: HealthDepartmentService, useValue: {} },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
