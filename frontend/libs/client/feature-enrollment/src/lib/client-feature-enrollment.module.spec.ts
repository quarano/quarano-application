import { async, TestBed } from '@angular/core/testing';
import { ClientFeatureEnrollmentModule } from './client-feature-enrollment.module';

describe('ClientFeatureEnrollmentModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientFeatureEnrollmentModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientFeatureEnrollmentModule).toBeDefined();
  });
});
