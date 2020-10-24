import { HealthDepartmentDto } from '@qro/client/domain';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'qro-health-department-address',
  templateUrl: './health-department-address.component.html',
  styleUrls: ['./health-department-address.component.scss'],
})
export class HealthDepartmentAddressComponent implements OnInit {
  healthDepartment: HealthDepartmentDto;

  constructor(private route: ActivatedRoute, private router: Router) {
    const queryParams = this.route.snapshot.queryParamMap;
    if (queryParams.has('healthDepartment')) {
      this.healthDepartment = JSON.parse(decodeURIComponent(queryParams.get('healthDepartment')));
    } else {
      this.router.navigate(['/']);
    }
  }

  ngOnInit() {}
}
