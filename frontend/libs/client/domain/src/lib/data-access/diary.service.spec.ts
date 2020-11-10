import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed, async, inject } from '@angular/core/testing';
import { DiaryService } from './diary.service';
import { API_URL } from '@qro/shared/util-data-access';

describe('Service: Diary', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [DiaryService, { provide: API_URL, useValue: '' }],
    });
  });

  it('should ...', inject([DiaryService], (service: DiaryService) => {
    expect(service).toBeTruthy();
  }));
});
