/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiaryEntryComponent } from './diary-entry.component';
import { RouterTestingModule } from '@angular/router/testing';
import { MatDialog } from '@angular/material/dialog';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { ApiService } from '../../../services/api.service';
import { SnackbarService } from '../../../services/snackbar.service';
import { DiaryEntryDto } from '../../../models/diary-entry';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('DiaryEntryComponent', () => {
  let component: DiaryEntryComponent;
  let fixture: ComponentFixture<DiaryEntryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [DiaryEntryComponent],
      providers: [
        FormBuilder,
        { provide: ApiService, useValue: { createDiaryEntry: () => { } } },
        { provide: SnackbarService, useValue: { warning: () => { }, success: () => { } } },
        { provide: MatDialog, useValue: {} },
        {
          provide: ActivatedRoute, useValue: {
            data: of({
              diaryEntry: { characteristicSymptoms: [], nonCharacteristicSymptoms: [], contacts: [] },
              symptoms: [],
              contactPersons: []
            }),
            snapshot: { paramMap: { get: () => '' } }
          }
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiaryEntryComponent);
    component = fixture.componentInstance;
    component.diaryEntry = {} as DiaryEntryDto;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
