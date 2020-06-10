import { async, TestBed } from '@angular/core/testing';
import { AdministrationFeatureAccountDetailModule } from './administration-feature-account-detail.module';

describe('AdministrationFeatureAccountDetailModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdministrationFeatureAccountDetailModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AdministrationFeatureAccountDetailModule).toBeDefined();
  });
});
