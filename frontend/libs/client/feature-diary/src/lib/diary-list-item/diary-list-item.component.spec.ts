import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { DiaryListItemComponent } from './diary-list-item.component';
import { DiaryListItemDto } from '@qro/client/domain';

describe('DiaryListItemComponent', () => {
  let component: DiaryListItemComponent;
  let fixture: ComponentFixture<DiaryListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DiaryListItemComponent],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiaryListItemComponent);
    component = fixture.componentInstance;
    component.diaryListItem = { morning: {}, evening: {}, date: '2020-03-01' } as DiaryListItemDto;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
