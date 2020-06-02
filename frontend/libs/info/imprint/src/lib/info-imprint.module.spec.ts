import { async, TestBed } from '@angular/core/testing';
import { InfoImprintModule } from './info-imprint.module';

describe('InfoImprintModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [InfoImprintModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(InfoImprintModule).toBeDefined();
  });
});
