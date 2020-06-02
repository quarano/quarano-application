import { async, TestBed } from '@angular/core/testing';
import { SharedUtilDataAccessModule } from './shared-util-data-access.module';

describe('SharedUtilDataAccessModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUtilDataAccessModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUtilDataAccessModule).toBeDefined();
  });
});
