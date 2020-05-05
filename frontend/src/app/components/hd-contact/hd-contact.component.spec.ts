import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HdContactComponent } from './hd-contact.component';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

describe('HdContactComponent', () => {
  let component: HdContactComponent;
  let fixture: ComponentFixture<HdContactComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HdContactComponent ],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: {} }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HdContactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
