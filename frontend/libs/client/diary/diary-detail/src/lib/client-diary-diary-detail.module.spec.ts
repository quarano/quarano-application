import { async, TestBed } from '@angular/core/testing';
import { ClientDiaryDiaryDetailModule } from './client-diary-diary-detail.module';

describe('ClientDiaryDiaryDetailModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientDiaryDiaryDetailModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientDiaryDiaryDetailModule).toBeDefined();
  });
});
