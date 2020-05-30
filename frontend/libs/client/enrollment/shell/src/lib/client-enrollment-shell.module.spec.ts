import { async, TestBed } from '@angular/core/testing';
import { ClientEnrollmentShellModule } from './client-enrollment-shell.module';

describe('ClientEnrollmentShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientEnrollmentShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientEnrollmentShellModule).toBeDefined();
  });
});
