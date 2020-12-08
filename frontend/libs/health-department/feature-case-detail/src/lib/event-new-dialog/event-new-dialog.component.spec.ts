import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventNewDialogComponent } from './event-new-dialog.component';

describe('EventNewDialogComponent', () => {
  let component: EventNewDialogComponent;
  let fixture: ComponentFixture<EventNewDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EventNewDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EventNewDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
