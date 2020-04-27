import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { CaseDetailDto } from '@models/case-detail';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { ApiService } from '@services/api.service';
import { SnackbarService } from '@services/snackbar.service';
import { CaseActionDto } from '@models/case-action';


@Component({
  selector: 'app-clients',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.scss']
})
export class ClientComponent implements OnInit {
  caseDetail$: Observable<CaseDetailDto>;
  caseAction$: Observable<CaseActionDto>;

  constructor(
    private route: ActivatedRoute, private router: Router,
    private apiService: ApiService, private snackbarService: SnackbarService) {
  }

  ngOnInit(): void {
    this.caseDetail$ = this.route.data.pipe(map((data) => data.case));
    this.caseAction$ = this.route.data.pipe(map((data) => data.actions));
  }


  saveCaseData(caseDetail: CaseDetailDto) {
    let saveData$: Observable<any>;
    if (!caseDetail.caseId) {
      saveData$ = this.apiService.createCase(caseDetail);
    } else {
      saveData$ = this.apiService.updateCase(caseDetail);
    }

    saveData$.pipe(take(1)).subscribe(() => {
      this.snackbarService.success('Persönliche Daten erfolgreich aktualisiert');
      this.router.navigate(['/tenant-admin/clients']);
    });
  }

}
