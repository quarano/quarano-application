import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OccasionListComponent } from './occasion-list.component';

describe('OccasionListComponent', () => {
  let component: OccasionListComponent;
  let fixture: ComponentFixture<OccasionListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OccasionListComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OccasionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
