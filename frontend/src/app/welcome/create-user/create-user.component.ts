import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { ClientDto } from '../../models/client';
import { QuestionnaireDto } from '../../models/first-query';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent implements OnInit {

  public stepperStarted = new Subject<boolean>();

  public client$$: BehaviorSubject<ClientDto> = new BehaviorSubject<ClientDto>(null);
  public firstQuery$$: BehaviorSubject<QuestionnaireDto> = new BehaviorSubject<QuestionnaireDto>(null);

  public registerStarted = false;
  public clientCode: string | undefined = undefined;

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

  public registerClient() {
    this.registerStarted = true;
    /*this.userService.createClientWithFirstQuery(this.client$$.getValue(), this.firstQuery$$.getValue())
      .subscribe(
        (client: Client) => {
          this.progressBarService.progressBarState = false;
          this.clientCode = client.clientCode;
          this.snackbarService.success('Die Registrierung war erfolgreich.');
        },
        error => {
          console.log(error);
          this.progressBarService.progressBarState = false;
          this.snackbarService.error('Es ist ein Fehler aufgetreten. Bitte später erneut versuchen.');
        }
      );*/
  }

  public navigateToDiary() {
    this.router.navigate(['/diary']);
  }

}
