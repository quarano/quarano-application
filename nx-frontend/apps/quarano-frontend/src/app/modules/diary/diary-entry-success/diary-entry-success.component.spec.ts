/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import {DebugElement, NO_ERRORS_SCHEMA} from '@angular/core';

import { DiaryEntrySuccessComponent } from './diary-entry-success.component';
import {DiaryEntryListItemDto} from '../../../models/diary-entry';

describe('DiaryEntrySuccessComponent', () => {
  let component: DiaryEntrySuccessComponent;
  let fixture: ComponentFixture<DiaryEntrySuccessComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiaryEntrySuccessComponent ],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiaryEntrySuccessComponent);
    component = fixture.componentInstance;
    component.entry = { contacts: [], symptoms: [], _links: { edit: {} }} as DiaryEntryListItemDto;
    component.label = '';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
