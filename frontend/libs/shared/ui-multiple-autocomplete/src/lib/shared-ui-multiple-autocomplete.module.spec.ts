import { async, TestBed } from '@angular/core/testing';
import { SharedUiMultipleAutocompleteModule } from './shared-ui-multiple-autocomplete.module';

describe('SharedUiMultipleAutocompleteModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUiMultipleAutocompleteModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUiMultipleAutocompleteModule).toBeDefined();
  });
});
