import { Component, OnInit } from '@angular/core';
import { UserService } from '@qro/auth/api';
import { ITileViewModel } from '@qro/shared/ui-tile';

@Component({
  selector: 'qro-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss'],
})
export class WelcomeComponent implements OnInit {
  tilesToShow: ITileViewModel[] = [];

  clientTiles: ITileViewModel[] = [
    {
      title: 'WELCOME.TAGEBUCH',
      subtitle: 'WELCOME.SYMPTOM_TRACKING',
      content: 'WELCOME.PFLEGEN_SIE',
      linkText: 'WELCOME.ZUM_TAGEBUCH',
      routerLink: ['/client/diary/diary-list'],
      headerImageUrl: 'assets/images/diary.png',
      backgroundImageUrl: 'assets/images/diary_tile_background.jpg',
    },
    {
      title: 'WELCOME.KONTAKTPERSONEN',
      subtitle: 'WELCOME.KONTAKTNACHVERFOLGUNG',
      content: 'WELCOME.VERWALTEN_SIE',
      linkText: 'WELCOME.ZU_DEN_KONTAKTPERSONEN',
      routerLink: ['/client/contact-persons/contact-person-list'],
      headerImageUrl: 'assets/images/contact-person.png',
      backgroundImageUrl: 'assets/images/contact_tile_background.jpg',
    },
  ];

  healthDepartmentTiles: ITileViewModel[] = [
    {
      title: 'WELCOME.INDEXFÄLLE',
      subtitle: 'WELCOME.VERWALTUNG_VON_INDEXFÄLLEN',
      content: 'WELCOME.SEHEN_SIE_INDEXFÄLLE',
      linkText: 'WELCOME.ZU_DEN_INDEXFÄLLEN',
      routerLink: ['/health-department/index-cases'],
      headerImageUrl: 'assets/images/diary.png',
      backgroundImageUrl: 'assets/images/clients_tile_background.jpg',
    },
    {
      title: 'WELCOME.KONTAKTPERSONEN',
      subtitle: 'WELCOME.VERWALTUNG_VON_KONTAKTPERSONEN',
      content: 'WELCOME.SEHEN_SIE_KONTAKTPERSONEN',
      linkText: 'WELCOME.ZU_DEN_KONTAKTPERSONEN',
      routerLink: ['/health-department/contact-cases'],
      headerImageUrl: 'assets/images/contact-person.png',
      backgroundImageUrl: 'assets/images/contact_tile_background.jpg',
    },
  ];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.tilesToShow = this.userService.isHealthDepartmentUser ? this.healthDepartmentTiles : this.clientTiles;
  }
}
