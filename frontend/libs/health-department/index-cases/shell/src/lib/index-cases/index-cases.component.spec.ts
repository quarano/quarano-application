import { RouterTestingModule } from '@angular/router/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IndexCasesComponent } from './index-cases.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('IndexCasesComponent', () => {
  let component: IndexCasesComponent;
  let fixture: ComponentFixture<IndexCasesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [IndexCasesComponent], schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IndexCasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
