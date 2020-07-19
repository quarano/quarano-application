import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { QuestionnaireDto, ApiService } from '@qro/shared/util-data-access';
import { SymptomDto } from '@qro/shared/util-symptom';
import { Observable } from 'rxjs';
import { map, switchMap, withLatestFrom } from 'rxjs/operators';

@Component({
  selector: 'qro-client-questionnaire',
  templateUrl: './questionnaire.component.html',
  styleUrls: ['./questionnaire.component.scss'],
})
export class QuestionnaireComponent implements OnInit {
  questionnaire$: Observable<QuestionnaireDto>;
  symptoms$: Observable<SymptomDto[]>;

  constructor(private route: ActivatedRoute, private apiService: ApiService) {}

  ngOnInit(): void {
    this.questionnaire$ = this.route.parent.data.pipe(
      map((data) => data.case),
      switchMap((data) => {
        if (data?._links?.hasOwnProperty('questionnaire')) {
          return this.apiService.getApiCall<QuestionnaireDto>(data, 'questionnaire');
        }
      })
    );

    this.symptoms$ = this.route.data.pipe(
      map((data) => data.symptoms),
      withLatestFrom(this.questionnaire$),
      map(([symptoms, questionnaire]) => {
        return symptoms.filter(
          (symptom) => questionnaire.symptoms.findIndex((symptomId) => symptomId === symptom.id) !== -1
        );
      })
    );
  }
}
