import { Component, Input, OnInit } from '@angular/core';
import { QuestionnaireDto } from '@qro/client/enrollment/domain';
import { SymptomDto } from '../../../../../../../../libs/shared/util-symptom/src/lib/models/symptom';

@Component({
  selector: 'qro-client-questionnaire',
  templateUrl: './questionnaire.component.html',
  styleUrls: ['./questionnaire.component.scss'],
})
export class QuestionnaireComponent implements OnInit {
  @Input()
  questionnaire: QuestionnaireDto;

  @Input()
  symptoms: SymptomDto[];

  constructor() {}

  ngOnInit(): void {}
}
