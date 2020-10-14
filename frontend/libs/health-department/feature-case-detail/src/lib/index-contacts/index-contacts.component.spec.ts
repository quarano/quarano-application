import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IndexContactsComponent } from './index-contacts.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

describe('IndexContactsComponent', () => {
  let component: IndexContactsComponent;
  let fixture: ComponentFixture<IndexContactsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [IndexContactsComponent],
      providers: [{ provide: ActivatedRoute, useValue: {} }],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IndexContactsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
