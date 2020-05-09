import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InitialQuestionaireFormComponent } from './initial-questionaire-form.component';

describe('InitialQuestionaireFormComponent', () => {
  let component: InitialQuestionaireFormComponent;
  let fixture: ComponentFixture<InitialQuestionaireFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InitialQuestionaireFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InitialQuestionaireFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
