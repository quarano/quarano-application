import { async, TestBed } from '@angular/core/testing';
import { SharedUtilSymptomModule } from './shared-util-symptom.module';

describe('SharedUtilSymptomModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUtilSymptomModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUtilSymptomModule).toBeDefined();
  });
});
