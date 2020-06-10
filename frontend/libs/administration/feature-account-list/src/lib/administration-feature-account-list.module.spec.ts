import { async, TestBed } from '@angular/core/testing';
import { AdministrationFeatureAccountListModule } from './administration-feature-account-list.module';

describe('AdministrationFeatureAccountListModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdministrationFeatureAccountListModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AdministrationFeatureAccountListModule).toBeDefined();
  });
});
