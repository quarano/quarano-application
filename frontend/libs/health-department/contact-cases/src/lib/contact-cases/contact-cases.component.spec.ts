import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContactCasesComponent } from './contact-cases.component';

describe('ContactCasesComponent', () => {
  let component: ContactCasesComponent;
  let fixture: ComponentFixture<ContactCasesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContactCasesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContactCasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
