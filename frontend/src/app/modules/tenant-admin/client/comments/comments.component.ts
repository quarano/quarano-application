import {Component, Input, OnInit} from '@angular/core';
import {CaseCommentDto} from '@models/case-comment';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-client-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent implements OnInit {

  @Input()
  comments: CaseCommentDto[];

  formGroup: FormGroup = new FormGroup({
    comment: new FormControl(null, [Validators.required])
  });

  constructor() {
  }

  ngOnInit(): void {
  }

  submitComment() {
    if (this.formGroup.valid) {
      alert('Send comment: ' + this.formGroup.get('comment').value);
    }
  }
}
