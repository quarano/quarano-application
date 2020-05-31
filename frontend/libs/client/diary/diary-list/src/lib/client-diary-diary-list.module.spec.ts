import { async, TestBed } from '@angular/core/testing';
import { ClientDiaryDiaryListModule } from './client-diary-diary-list.module';

describe('ClientDiaryDiaryListModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientDiaryDiaryListModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientDiaryDiaryListModule).toBeDefined();
  });
});
