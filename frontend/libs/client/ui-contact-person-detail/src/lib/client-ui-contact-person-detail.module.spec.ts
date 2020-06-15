import { async, TestBed } from '@angular/core/testing';
import { ClientUiContactPersonDetailModule } from './client-ui-contact-person-detail.module';

describe('ClientUiContactPersonDetailModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientUiContactPersonDetailModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientUiContactPersonDetailModule).toBeDefined();
  });
});
