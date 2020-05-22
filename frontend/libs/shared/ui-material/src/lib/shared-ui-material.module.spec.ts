import { async, TestBed } from '@angular/core/testing';
import { SharedUiMaterialModule } from './shared-ui-material.module';

describe('SharedUiMaterialModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUiMaterialModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUiMaterialModule).toBeDefined();
  });
});
