import { async, TestBed } from '@angular/core/testing';
import { ClientProfileUiPersonalDataModule } from './client-profile-ui-personal-data.module';

describe('ClientProfileUiPersonalDataModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientProfileUiPersonalDataModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientProfileUiPersonalDataModule).toBeDefined();
  });
});
