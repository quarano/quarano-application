import { async, TestBed } from '@angular/core/testing';
import { SharedUtilTranslationModule } from './shared-util-translation.module';

describe('SharedUtilTranslationModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUtilTranslationModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUtilTranslationModule).toBeDefined();
  });
});
