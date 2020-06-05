import { async, TestBed } from '@angular/core/testing';
import { ClientDiaryDomainModule } from './client-diary-domain.module';

describe('ClientDiaryDomainModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientDiaryDomainModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientDiaryDomainModule).toBeDefined();
  });
});
