import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseDetailComponent } from './case-detail.component';
import { RouterTestingModule } from '@angular/router/testing';
import { MatDialog } from '@angular/material/dialog';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ApiService } from '../../../../../../../apps/quarano-frontend/src/app/services/api.service';
import { SnackbarService } from '@qro/shared/util';

describe('ClientComponent', () => {
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
        { provide: MatDialog, useValue: {} },
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
