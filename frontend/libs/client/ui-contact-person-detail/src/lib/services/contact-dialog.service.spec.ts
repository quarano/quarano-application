import { TestBed } from '@angular/core/testing';

import { ContactDialogService } from './contact-dialog.service';
import { MatDialogModule } from '@angular/material/dialog';

describe('QroDialogService', () => {
  let service: ContactDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatDialogModule],
    });
    service = TestBed.inject(ContactDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
