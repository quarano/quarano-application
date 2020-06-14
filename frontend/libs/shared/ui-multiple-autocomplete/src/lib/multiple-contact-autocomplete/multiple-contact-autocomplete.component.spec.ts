import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultipleContactAutocompleteComponent } from './multiple-contact-autocomplete.component';
import { MultipleAutocompleteComponent } from '../multiple-autocomplete/multiple-autocomplete.component';

describe('MultipleContactAutocompleteComponent', () => {
  let component: MultipleContactAutocompleteComponent;
  let fixture: ComponentFixture<MultipleContactAutocompleteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MultipleContactAutocompleteComponent, MultipleAutocompleteComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultipleContactAutocompleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
