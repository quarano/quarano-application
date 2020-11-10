import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { map } from 'rxjs/operators';

@Component({
  selector: 'qro-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss'],
})
export class ErrorComponent implements OnInit {
  message$: Observable<string>;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.message$ = this.route.queryParamMap.pipe(
      map((paramMap) => paramMap.get('message') || 'ERROR.UNBEKANNTER_FEHLER')
    );
  }
}
