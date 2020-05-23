import { Router, ActivatedRoute } from '@angular/router';
import { Component, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import { CaseCommentDto } from '../../../../models/case-comment';
import { TrimmedPatternValidator } from '../../../../validators/trimmed-pattern.validator';
import { VALIDATION_PATTERNS } from '../../../../validators/validation-patterns';

@Component({
  selector: 'qro-client-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent {

  @Input()
  comments: CaseCommentDto[];

  @Output()
  newComment: Subject<string> = new Subject<string>();

  formGroup: FormGroup = new FormGroup({
    comment: new FormControl(null, [Validators.required, TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual)])
  });

  submitComment() {
    if (this.formGroup.valid) {
      this.newComment.next(this.formGroup.get('comment').value);
    }
  }
}
