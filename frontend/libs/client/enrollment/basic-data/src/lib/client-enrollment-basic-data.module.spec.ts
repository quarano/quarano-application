import { async, TestBed } from '@angular/core/testing';
import { ClientEnrollmentBasicDataModule } from './client-enrollment-basic-data.module';

describe('ClientEnrollmentBasicDataModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientEnrollmentBasicDataModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientEnrollmentBasicDataModule).toBeDefined();
  });
});
