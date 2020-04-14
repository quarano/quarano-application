import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
  animations: [
    trigger('toggleMenu', [
      state('open', style({
        transform: 'scaleY(1)',
        opacity: 1
      })),
      state('closed', style({
        transform: 'scaleY(0)',
        opacity: 0.4
      })),
      transition('open => closed', [
        animate('0.1s')
      ]),
      transition('closed => open', [
        animate('0.1s')
      ]),
    ])
  ]
})
export class LandingComponent implements OnInit {

  public showMobileMenu = false;

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  public toggleMobileMenu() {
    this.showMobileMenu = !this.showMobileMenu;
  }

  public navigate(path: string) {
    this.showMobileMenu = false;
    this.router.navigate([path]);
  }

}
