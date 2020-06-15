import { async, TestBed } from '@angular/core/testing';
import { ClientFeatureDiaryModule } from './client-feature-diary.module';

describe('ClientFeatureDiaryModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientFeatureDiaryModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientFeatureDiaryModule).toBeDefined();
  });
});
