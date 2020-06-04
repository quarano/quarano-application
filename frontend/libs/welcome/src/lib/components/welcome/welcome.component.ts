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
      title: 'Tagebuch',
      subtitle: 'Symptom-Tracking',
      content:
        'Pflegen Sie zwei Mal täglich in Ihrem Tagebuch die eventuell auftretenden Symptome und Ihre Körpertemperatur.',
      linkText: 'Zum Tagebuch',
      routerLink: ['/client/diary/diary-list'],
      headerImageUrl: '/assets/images/diary.png',
      backgroundImageUrl: '/assets/images/diary_tile_background.jpg',
    },
    {
      title: 'Kontaktpersonen',
      subtitle: 'Kontaktnachverfolgung',
      content:
        'Verwalten Sie Ihre persönlichen Kontaktpersonen, damit Sie diese einfach und schnell in den Tagebucheinträgen referenzieren können, ohne sie neu eingeben zu müssen.',
      linkText: 'Zu den Kontaktpersonen',
      routerLink: ['/client/contact-persons/contact-person-list'],
      headerImageUrl: '/assets/images/contact-person.png',
      backgroundImageUrl: '/assets/images/contact_tile_background.jpg',
    },
  ];

  healthDepartmentTiles: ITileViewModel[] = [
    {
      title: 'Indexfälle',
      subtitle: 'Verwaltung von Indexfällen',
      content:
        'Sehen und bearbeiten Sie hier alle offenen Indexfälle in Ihrem Gesundheitsamt. ' +
        'Legen Sie neue Fälle an und nutzen Sie die Kontaktfunktion.',
      linkText: 'Zu den Indexfällen',
      routerLink: ['/health-department/index-cases'],
      headerImageUrl: '/assets/images/diary.png',
      backgroundImageUrl: '/assets/images/clients_tile_background.jpg',
    },
    {
      title: 'Kontaktpersonen',
      subtitle: 'Verwaltung von Kontaktpersonen',
      content:
        'Sehen und bearbeiten Sie hier alle offenen Kontaktpersonen in Ihrem Gesundheitsamt. ' +
        'Legen Sie neue Fälle an und nutzen Sie die Kontaktfunktion.',
      linkText: 'Zu den Kontaktpersonen',
      routerLink: ['/health-department/contact-cases'],
      headerImageUrl: '/assets/images/contact-person.png',
      backgroundImageUrl: '/assets/images/contact_tile_background.jpg',
    },
  ];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.tilesToShow = this.userService.isHealthDepartmentUser ? this.healthDepartmentTiles : this.clientTiles;
  }
}
