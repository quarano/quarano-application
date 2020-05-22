import { RouterTestingModule } from '@angular/router/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContactCasesComponent } from './contact-cases.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('ContactCasesComponent', () => {
  let component: ContactCasesComponent;
  let fixture: ComponentFixture<ContactCasesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [ContactCasesComponent], schemas: [NO_ERRORS_SCHEMA]
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
