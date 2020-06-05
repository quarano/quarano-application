import { async, TestBed } from '@angular/core/testing';
import { InfoApiModule } from './info-api.module';

describe('InfoApiModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [InfoApiModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(InfoApiModule).toBeDefined();
  });
});
