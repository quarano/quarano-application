import { async, TestBed } from '@angular/core/testing';
import { SharedUiErrorModule } from './shared-ui-error.module';

describe('SharedUiErrorModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUiErrorModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUiErrorModule).toBeDefined();
  });
});
