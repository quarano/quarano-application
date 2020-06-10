import { async, TestBed } from '@angular/core/testing';
import { SharedUtilFormsModule } from './shared-util-forms.module';

describe('SharedUtilFormsModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedUtilFormsModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SharedUtilFormsModule).toBeDefined();
  });
});
