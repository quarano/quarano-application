import { async, TestBed } from '@angular/core/testing';
import { ClientDomainModule } from './client-domain.module';

describe('ClientDomainModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientDomainModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientDomainModule).toBeDefined();
  });
});
