import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {QuestionnaireDto} from '../../../../models/first-query';
import {SymptomDto} from '../../../../models/symptom';

@Component({
  selector: 'qro-client-questionnaire',
  templateUrl: './questionnaire.component.html',
  styleUrls: ['./questionnaire.component.css']
})
export class QuestionnaireComponent implements OnInit {

  @Input()
  questionnaire: QuestionnaireDto;

  @Input()
  symptoms: SymptomDto[];

  constructor() { }

  ngOnInit(): void {
  }

}
