import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IndexContactsComponent } from './index-contacts.component';
import {NO_ERRORS_SCHEMA} from '@angular/core';

describe('IndexContactsComponent', () => {
  let component: IndexContactsComponent;
  let fixture: ComponentFixture<IndexContactsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IndexContactsComponent ],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IndexContactsComponent);
    component = fixture.componentInstance;
    component.contacts = [];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
