import { async, TestBed } from '@angular/core/testing';
import { ClientEnrollmentLandingModule } from './client-enrollment-landing.module';

describe('ClientEnrollmentLandingModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientEnrollmentLandingModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientEnrollmentLandingModule).toBeDefined();
  });
});
