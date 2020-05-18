/* tslint:disable:no-unused-variable */

import {inject, TestBed} from '@angular/core/testing';
import {ApiService} from './api.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('Service: Api', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ApiService]
    });
  });

  it('should ...', inject([ApiService], (service: ApiService) => {
    expect(service).toBeTruthy();
  }));
});
