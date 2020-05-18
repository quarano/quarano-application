/* tslint:disable:no-unused-variable */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {NO_ERRORS_SCHEMA} from '@angular/core';

import {AnomalyComponent} from './anomaly.component';

describe('AnomalyComponent', () => {
  let component: AnomalyComponent;
  let fixture: ComponentFixture<AnomalyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AnomalyComponent],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnomalyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
