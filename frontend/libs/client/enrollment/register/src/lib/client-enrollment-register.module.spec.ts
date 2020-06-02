import { async, TestBed } from '@angular/core/testing';
import { ClientEnrollmentRegisterModule } from './client-enrollment-register.module';

describe('ClientEnrollmentRegisterModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientEnrollmentRegisterModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientEnrollmentRegisterModule).toBeDefined();
  });
});
