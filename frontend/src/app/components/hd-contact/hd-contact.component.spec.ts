import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HdContactComponent } from './hd-contact.component';

describe('HdContactComponent', () => {
  let component: HdContactComponent;
  let fixture: ComponentFixture<HdContactComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HdContactComponent ]
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
