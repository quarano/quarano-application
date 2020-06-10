import { async, TestBed } from '@angular/core/testing';
import { SharedUiDataProtectionModule } from './shared-ui-data-protection.module';

describe('SharedUiDataProtectionModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUiDataProtectionModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUiDataProtectionModule).toBeDefined();
  });
});
