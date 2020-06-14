import { TestBed } from '@angular/core/testing';

import { QroDialogService } from './qro-dialog.service';
import { MatDialogModule } from '@angular/material/dialog';

describe('QroDialogService', () => {
  let service: QroDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatDialogModule],
    });
    service = TestBed.inject(QroDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
