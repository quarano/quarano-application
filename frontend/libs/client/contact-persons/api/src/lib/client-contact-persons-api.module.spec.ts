import { async, TestBed } from '@angular/core/testing';
import { ClientContactPersonsApiModule } from './client-contact-persons-api.module';

describe('ClientContactPersonsApiModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientContactPersonsApiModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientContactPersonsApiModule).toBeDefined();
  });
});
