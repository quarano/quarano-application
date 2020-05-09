import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IndexContactsComponent } from './index-contacts.component';

describe('IndexContactsComponent', () => {
  let component: IndexContactsComponent;
  let fixture: ComponentFixture<IndexContactsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IndexContactsComponent ]
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
