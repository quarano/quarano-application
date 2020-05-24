import { async, TestBed } from '@angular/core/testing';
import { SharedUtilFormValidationModule } from './shared-util-form-validation.module';

describe('SharedUtilFormValidationModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUtilFormValidationModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUtilFormValidationModule).toBeDefined();
  });
});
