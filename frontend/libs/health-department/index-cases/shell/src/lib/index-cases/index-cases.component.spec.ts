import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IndexCasesComponent } from './index-cases.component';

describe('IndexCasesComponent', () => {
  let component: IndexCasesComponent;
  let fixture: ComponentFixture<IndexCasesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IndexCasesComponent ]
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
