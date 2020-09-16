import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContactLocationFormComponent } from './contact-location-form.component';

describe('ContactLocationFormComponent', () => {
  let component: ContactLocationFormComponent;
  let fixture: ComponentFixture<ContactLocationFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ContactLocationFormComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContactLocationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
