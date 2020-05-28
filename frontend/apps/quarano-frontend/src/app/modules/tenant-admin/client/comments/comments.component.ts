import { ValidationErrorGenerator, VALIDATION_PATTERNS, TrimmedPatternValidator } from '@qro/shared/util-form-validation';
import { Component, Input, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import { CaseCommentDto } from '../../../../models/case-comment';

@Component({
  selector: 'qro-client-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent {
  errorGenerator = ValidationErrorGenerator;
  @Input()
  comments: CaseCommentDto[];
  @Input() loading: boolean;

  @Output()
  newComment: Subject<string> = new Subject<string>();

  formGroup: FormGroup = new FormGroup({
    comment: new FormControl(null, [Validators.required, TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual)])
  });

  submitComment() {
    if (this.formGroup.valid) {
      this.newComment.next(this.formGroup.get('comment').value);
      this.formGroup.reset();
    }
  }
}
