import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  public contactForm = new FormGroup({
    name: new FormControl(null, [Validators.required]),
    email: new FormControl(null, [Validators.email]),
    phone: new FormControl(null),
    message: new FormControl(null, [Validators.required]),
  });

  constructor() { }

  ngOnInit(): void {
  }

  public submitForm() {
    this.contactForm.reset();
  }

}
