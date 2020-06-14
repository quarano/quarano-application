import { TestBed } from '@angular/core/testing';

import { QroDialogService } from './qro-dialog.service';

describe('QroDialogServiceService', () => {
  let service: QroDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QroDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
