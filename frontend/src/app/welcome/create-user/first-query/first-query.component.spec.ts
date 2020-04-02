import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FirstQueryComponent } from './first-query.component';

describe('FirstQueryComponent', () => {
  let component: FirstQueryComponent;
  let fixture: ComponentFixture<FirstQueryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FirstQueryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FirstQueryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
