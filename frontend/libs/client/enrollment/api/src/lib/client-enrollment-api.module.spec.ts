import { async, TestBed } from '@angular/core/testing';
import { ClientEnrollmentApiModule } from './client-enrollment-api.module';

describe('ClientEnrollmentApiModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientEnrollmentApiModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientEnrollmentApiModule).toBeDefined();
  });
});
