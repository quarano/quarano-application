import { async, TestBed } from '@angular/core/testing';
import { SharedUtilDateModule } from './shared-util-date.module';

describe('SharedUtilDateModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUtilDateModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUtilDateModule).toBeDefined();
  });
});
