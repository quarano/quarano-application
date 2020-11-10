import { TranslateTestingModule } from '@qro/shared/util-translation';
/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { DiaryEntryWarningComponent } from './diary-entry-warning.component';

describe('DiaryEntryWarningComponent', () => {
  let component: DiaryEntryWarningComponent;
  let fixture: ComponentFixture<DiaryEntryWarningComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TranslateTestingModule],
      declarations: [DiaryEntryWarningComponent],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiaryEntryWarningComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
