import { async, TestBed } from '@angular/core/testing';
import { SharedUiTileModule } from './shared-ui-tile.module';

describe('SharedUiTileModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUiTileModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUiTileModule).toBeDefined();
  });
});
