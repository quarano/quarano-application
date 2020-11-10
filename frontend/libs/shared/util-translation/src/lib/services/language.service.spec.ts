import { TranslateService } from '@ngx-translate/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed, inject } from '@angular/core/testing';
import { LanguageService } from './language.service';
import { provideMockStore } from '@ngrx/store/testing';

describe('Service: Language', () => {
  const initialState = { selectedLanguage: undefined, supportedLanguages: [] };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [LanguageService, provideMockStore({ initialState }), { provide: TranslateService, useValue: {} }],
    });
  });

  it('should ...', inject([LanguageService], (service: LanguageService) => {
    expect(service).toBeTruthy();
  }));
});
