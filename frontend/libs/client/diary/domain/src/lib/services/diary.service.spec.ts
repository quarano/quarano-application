/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { DiaryService } from './diary.service';

describe('Service: Diary', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DiaryService],
    });
  });

  it('should ...', inject([DiaryService], (service: DiaryService) => {
    expect(service).toBeTruthy();
  }));
});
