import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseListComponent } from './case-list.component';

describe('CaseListComponent', () => {
  let component: CaseListComponent;
  let fixture: ComponentFixture<CaseListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CaseListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CaseListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
