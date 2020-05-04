/* tslint:disable:no-unused-variable */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DiaryEntryComponent} from './diary-entry.component';
import {RouterTestingModule} from '@angular/router/testing';
import {ApiService} from '@services/api.service';
import {SnackbarService} from '@services/snackbar.service';
import {MatDialog} from '@angular/material/dialog';
import {FormBuilder} from '@angular/forms';
import {DiaryEntryDto} from '@models/diary-entry';
import {ActivatedRoute} from '@angular/router';
import {of} from 'rxjs';

describe('DiaryEntryComponent', () => {
  let component: DiaryEntryComponent;
  let fixture: ComponentFixture<DiaryEntryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [DiaryEntryComponent],
      providers: [
        FormBuilder,
        {provide: ApiService, useValue: jasmine.createSpyObj(['createDiaryEntry'])},
        {provide: SnackbarService, useValue: jasmine.createSpyObj(['warning', 'success'])},
        {provide: MatDialog, useValue: {}},
        {
          provide: ActivatedRoute, useValue: {
            data: of({
              diaryEntry: {characteristicSymptoms: [], nonCharacteristicSymptoms: [], contacts: []},
              symptoms: [],
              contactPersons: []
            }),
            snapshot: {paramMap: {get: (value) => ''}}
          }
        }
      ]
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
