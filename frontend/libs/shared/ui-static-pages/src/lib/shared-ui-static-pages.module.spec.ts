import { async, TestBed } from '@angular/core/testing';
import { SharedUiStaticPagesModule } from './shared-ui-static-pages.module';

describe('SharedUiStaticPagesModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUiStaticPagesModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUiStaticPagesModule).toBeDefined();
  });
});
