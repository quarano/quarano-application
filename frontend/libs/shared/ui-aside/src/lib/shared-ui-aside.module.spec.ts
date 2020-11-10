import { async, TestBed } from '@angular/core/testing';
import { SharedUiAsideModule } from './shared-ui-aside.module';

describe('SharedUiAsideModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUiAsideModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUiAsideModule).toBeDefined();
  });
});
