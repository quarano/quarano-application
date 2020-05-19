import { async, TestBed } from '@angular/core/testing';
import { SharedUtilModule } from './shared-util.module';

describe('SharedUtilModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUtilModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUtilModule).toBeDefined();
  });
});
