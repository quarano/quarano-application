import { async, TestBed } from '@angular/core/testing';
import { InfoDataProtectionModule } from './info-data-protection.module';

describe('InfoDataProtectionModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [InfoDataProtectionModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(InfoDataProtectionModule).toBeDefined();
  });
});
