import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OccasionCardComponent } from './occasion-card.component';

describe('occasionCardComponent', () => {
  let component: OccasionCardComponent;
  let fixture: ComponentFixture<OccasionCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OccasionCardComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OccasionCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
