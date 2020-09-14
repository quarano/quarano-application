import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {NO_ERRORS_SCHEMA} from '@angular/core';
import {DiaryEntriesListComponent} from "./diary-entries-list.component";

describe('DiaryEntriesListComponent', () => {
  let component: DiaryEntriesListComponent;
  let fixture: ComponentFixture<DiaryEntriesListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DiaryEntriesListComponent],
      imports: [],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiaryEntriesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
