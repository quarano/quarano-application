import { TranslateTestingModule } from '@qro/shared/util-translation';
import { MatDialogRef } from '@angular/material/dialog';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';

import { DataProtectionDialogComponent } from './data-protection-dialog.component';

describe('DataProtectionDialogComponent', () => {
  let component: DataProtectionDialogComponent;
  let fixture: ComponentFixture<DataProtectionDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TranslateTestingModule],
      declarations: [DataProtectionDialogComponent],
      providers: [{ provide: MatDialogRef, useValue: {} }],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataProtectionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
