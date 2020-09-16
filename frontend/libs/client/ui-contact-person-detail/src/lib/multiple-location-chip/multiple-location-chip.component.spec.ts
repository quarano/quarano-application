import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultipleLocationChipComponent } from './multiple-location-chip.component';

describe('MultipleLocationChipComponent', () => {
  let component: MultipleLocationChipComponent;
  let fixture: ComponentFixture<MultipleLocationChipComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MultipleLocationChipComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultipleLocationChipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
