import { async, TestBed } from '@angular/core/testing';
import { SharedUtilCommonFunctionsModule } from './shared-util-common-functions.module';

describe('SharedUtilCommonFunctionsModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUtilCommonFunctionsModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUtilCommonFunctionsModule).toBeDefined();
  });
});
