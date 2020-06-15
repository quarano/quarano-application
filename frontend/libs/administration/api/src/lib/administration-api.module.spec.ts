import { async, TestBed } from '@angular/core/testing';
import { AdministrationApiModule } from './administration-api.module';

describe('AdministrationApiModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdministrationApiModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AdministrationApiModule).toBeDefined();
  });
});
