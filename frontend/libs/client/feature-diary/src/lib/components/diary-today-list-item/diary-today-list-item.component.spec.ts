import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DiaryTodayListItemComponent } from './diary-today-list-item.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { DiaryListItemDto } from '@qro/client/diary/domain';

describe('DiaryTodayListItemComponent', () => {
  let component: DiaryTodayListItemComponent;
  let fixture: ComponentFixture<DiaryTodayListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DiaryTodayListItemComponent],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiaryTodayListItemComponent);
    component = fixture.componentInstance;
    component.diaryListItem = { date: '2020-03-01', evening: {}, morning: {} } as DiaryListItemDto;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
