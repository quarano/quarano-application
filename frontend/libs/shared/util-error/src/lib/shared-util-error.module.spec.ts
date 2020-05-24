import { async, TestBed } from '@angular/core/testing';
import { SharedUtilErrorModule } from './shared-util-error.module';

describe('SharedUtilErrorModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUtilErrorModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUtilErrorModule).toBeDefined();
  });
});
