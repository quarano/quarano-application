import { Store, select } from '@ngrx/store';
import { CaseEntityService } from '@qro/health-department/domain';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { QuestionnaireDto, ApiService } from '@qro/shared/util-data-access';
import { SymptomDto, SymptomSelectors } from '@qro/shared/util-symptom';
import { Observable, combineLatest } from 'rxjs';
import { map, switchMap, withLatestFrom, shareReplay } from 'rxjs/operators';

@Component({
  selector: 'qro-client-questionnaire',
  templateUrl: './questionnaire.component.html',
  styleUrls: ['./questionnaire.component.scss'],
})
export class QuestionnaireComponent implements OnInit {
  questionnaire$: Observable<QuestionnaireDto>;
  symptoms$: Observable<SymptomDto[]>;

  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService,
    private entityService: CaseEntityService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.questionnaire$ = combineLatest([
      this.route.parent.paramMap.pipe(map((paramMap) => paramMap.get('id'))),
      this.entityService.entityMap$,
    ]).pipe(
      map(([id, entityMap]) => {
        return entityMap[id];
      }),
      shareReplay(1),
      switchMap((data) => {
        if (data?._links?.hasOwnProperty('questionnaire')) {
          return this.apiService.getApiCall<QuestionnaireDto>(data, 'questionnaire');
        }
      })
    );

    this.symptoms$ = this.store.pipe(
      select(SymptomSelectors.symptoms),
      withLatestFrom(this.questionnaire$),
      map(([symptoms, questionnaire]) => {
        return symptoms.filter(
          (symptom) => questionnaire.symptoms.findIndex((symptomId) => symptomId === symptom.id) !== -1
        );
      })
    );
  }
}
