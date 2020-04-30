import {Component, Input, OnInit, Output} from '@angular/core';
import {CaseCommentDto} from '@models/case-comment';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Subject} from 'rxjs';

@Component({
  selector: 'app-client-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent implements OnInit {

  @Input()
  comments: CaseCommentDto[];

  @Output()
  newComment: Subject<string> = new Subject<string>();

  formGroup: FormGroup = new FormGroup({
    comment: new FormControl(null, [Validators.required])
  });

  constructor() {
  }

  ngOnInit(): void {
  }

  submitComment() {
    if (this.formGroup.valid) {
      this.newComment.next(this.formGroup.get('comment').value);
    }
  }
}
