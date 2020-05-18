/* tslint:disable:no-unused-variable */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AgbComponent} from './agb.component';
import {NO_ERRORS_SCHEMA} from '@angular/core';

describe('AgbComponent', () => {
  let component: AgbComponent;
  let fixture: ComponentFixture<AgbComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AgbComponent],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
