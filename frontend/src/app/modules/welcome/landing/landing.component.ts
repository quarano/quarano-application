import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {

  public userType = 'index';
  public clientcode;

  constructor(private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.userType = this.route.snapshot.paramMap.get('usertype') || this.userType;
    this.clientcode = this.route.snapshot.paramMap.get('clientcode');
  }
}
