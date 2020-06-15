import { async, TestBed } from '@angular/core/testing';
import { SharedUtilProgressBarModule } from './shared-util-progress-bar.module';

describe('SharedUtilProgressBarModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUtilProgressBarModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUtilProgressBarModule).toBeDefined();
  });
});
