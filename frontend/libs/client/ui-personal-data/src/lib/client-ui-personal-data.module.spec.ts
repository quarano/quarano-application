import { async, TestBed } from '@angular/core/testing';
import { ClientUiPersonalDataModule } from './client-ui-personal-data.module';

describe('ClientUiPersonalDataModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientUiPersonalDataModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientUiPersonalDataModule).toBeDefined();
  });
});
