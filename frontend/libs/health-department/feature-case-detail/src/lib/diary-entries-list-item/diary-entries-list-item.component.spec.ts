import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {NO_ERRORS_SCHEMA} from '@angular/core';
import {DiaryEntriesListItemComponent} from "./diary-entries-list-item.component";

describe('ContactListComponent', () => {
  let component: DiaryEntriesListItemComponent;
  let fixture: ComponentFixture<DiaryEntriesListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DiaryEntriesListItemComponent],
      imports: [],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiaryEntriesListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
