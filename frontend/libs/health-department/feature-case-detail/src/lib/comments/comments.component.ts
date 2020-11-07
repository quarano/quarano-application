import { HealthDepartmentService } from '@qro/health-department/api';
import { SubSink } from 'subsink';
import { ActivatedRoute } from '@angular/router';
import { TrimmedPatternValidator, VALIDATION_PATTERNS, ValidationErrorService } from '@qro/shared/util-forms';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { CaseCommentDto, CaseEntityService } from '@qro/health-department/domain';
import { finalize, map, shareReplay, switchMap, tap } from 'rxjs/operators';
import { SnackbarService } from '@qro/shared/util-snackbar';

@Component({
  selector: 'qro-client-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss'],
})
export class CommentsComponent implements OnInit, OnDestroy {
  comments$: Observable<CaseCommentDto[]>;
  loading: boolean;
  private subs = new SubSink();
  private caseId: string;

  formGroup: FormGroup = new FormGroup({
    comment: new FormControl(null, [
      Validators.required,
      TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual),
    ]),
  });

  constructor(
    private route: ActivatedRoute,
    private healthDepartmentService: HealthDepartmentService,
    private snackbarService: SnackbarService,
    private entityService: CaseEntityService,
    public validationErrorService: ValidationErrorService
  ) {}

  ngOnInit(): void {
    this.comments$ = this.route.parent.paramMap.pipe(
      tap((params) => (this.caseId = params.get('id'))),
      switchMap((params) => this.entityService.loadOneFromStore(params.get('id'))),
      map((caseDto) => {
        return caseDto.comments;
      }),
      shareReplay(1)
    );
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  submitComment() {
    this.addComment(this.formGroup.get('comment').value);
  }

  addComment(commentText: string) {
    this.loading = true;
    this.healthDepartmentService
      .addComment(this.caseId, commentText)
      .pipe(
        tap((data) => this.entityService.updateOneInCache(data)),
        finalize(() => (this.loading = false))
      )
      .subscribe(() => {
        this.snackbarService.success('Kommentar erfolgreich eingetragen.');
        this.formGroup.reset();
      });
  }
}
