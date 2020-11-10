import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'qro-forbidden',
  templateUrl: './forbidden.component.html',
  styleUrls: ['./forbidden.component.scss'],
})
export class ForbiddenComponent implements OnInit {
  message$: Observable<string>;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.message$ = this.route.queryParamMap.pipe(
      map((paramMap) => {
        if (paramMap.has('message')) {
          return decodeURIComponent(paramMap.get('message'));
        }
        return 'FORBIDDEN.SIE_HABEN_NICHT_DIE_ERFORDERLICHEN_BERECHTIGUNGEN';
      })
    );
  }
}
