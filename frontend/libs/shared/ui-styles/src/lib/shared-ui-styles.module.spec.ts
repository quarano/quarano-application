import { async, TestBed } from '@angular/core/testing';
import { SharedUiStylesModule } from './shared-ui-styles.module';

describe('SharedUiStylesModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUiStylesModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUiStylesModule).toBeDefined();
  });
});
