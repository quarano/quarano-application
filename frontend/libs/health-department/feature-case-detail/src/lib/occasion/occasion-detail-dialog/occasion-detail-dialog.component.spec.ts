import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OccasionDetailDialogComponent } from './occasion-detail-dialog.component';

describe('OccasionDetailDialogComponent', () => {
  let component: OccasionDetailDialogComponent;
  let fixture: ComponentFixture<OccasionDetailDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OccasionDetailDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OccasionDetailDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
