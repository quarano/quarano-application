import { Component, Input } from '@angular/core';
import { QuestionnaireDto } from '@qro/client/api';
import { SymptomDto } from '@qro/shared/util-symptom';

@Component({
  selector: 'qro-client-questionnaire',
  templateUrl: './questionnaire.component.html',
  styleUrls: ['./questionnaire.component.scss'],
})
export class QuestionnaireComponent {
  @Input()
  questionnaire: QuestionnaireDto;

  @Input()
  symptoms: SymptomDto[];
}
