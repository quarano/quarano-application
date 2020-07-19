import { HealthDepartmentService } from '@qro/health-department/api';
import { SubSink } from 'subsink';
import { ActivatedRoute } from '@angular/router';
import { ValidationErrorGenerator, VALIDATION_PATTERNS, TrimmedPatternValidator } from '@qro/shared/util-forms';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { CaseCommentDto, CaseDto } from '@qro/health-department/domain';
import { map, tap, finalize } from 'rxjs/operators';
import { SnackbarService } from '@qro/shared/util-snackbar';

@Component({
  selector: 'qro-client-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss'],
})
export class CommentsComponent implements OnInit, OnDestroy {
  errorGenerator = ValidationErrorGenerator;
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
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {
    this.comments$ = this.route.parent.data.pipe(
      map((data) => data.case as CaseDto),
      map((data) => data?.comments)
    );

    this.caseId = this.route.parent.snapshot.paramMap.get('id');
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  submitComment() {
    if (this.formGroup.valid) {
      this.addComment(this.formGroup.get('comment').value);
      this.formGroup.reset();
    }
  }

  addComment(commentText: string) {
    this.loading = true;
    this.comments$ = this.healthDepartmentService.addComment(this.caseId, commentText).pipe(
      map((data) => data.comments),
      tap((data) => this.snackbarService.success('Kommentar erfolgreich eingetragen.')),
      finalize(() => (this.loading = false))
    );
  }
}
