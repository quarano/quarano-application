import { Component, OnInit } from '@angular/core';
import { ITileViewModel } from '../../ui/tile/tile.component';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'qro-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {
  tilesToShow: ITileViewModel[] = [];

  clientTiles: ITileViewModel[] = [
    {
      title: 'Tagebuch',
      subtitle: 'Symptom-Tracking',
      content: 'Pflegen Sie zwei Mal täglich in Ihrem Tagebuch die eventuell auftretenden Symptome und Ihre Körpertemperatur.',
      linkText: 'Zum Tagebuch',
      routerLink: ['/diary'],
      headerImageUrl: '/assets/images/diary.png',
      backgroundImageUrl: '/assets/images/diary_tile_background.jpg'
    },
    {
      title: 'Kontaktpersonen',
      subtitle: 'Kontaktnachverfolgung',
      content: 'Verwalten Sie Ihre persönlichen Kontaktpersonen, damit Sie diese einfach und schnell in den Tagebucheinträgen referenzieren können, ohne sie neu eingeben zu müssen.',
      linkText: 'Zu den Kontaktpersonen',
      routerLink: ['/contact-persons'],
      headerImageUrl: '/assets/images/contact-person.png',
      backgroundImageUrl: '/assets/images/contact_tile_background.jpg'
    }
  ];

  healthDepartmentTiles: ITileViewModel[] = [
    {
      title: 'Fallübersicht',
      subtitle: 'Liste aller offenen Index- und Kontaktfälle',
      content: 'Sehen und bearbeiten Sie hier alle offenen Index- und Kontaktfälle in Ihrem Gesundheitsamt. ' +
        'Legen Sie neue Fälle an und nutzen Sie die Kontaktfunktion. Exportieren Sie die Liste in eine csv Datei.',
      linkText: 'Zur Fallübersicht',
      routerLink: ['/tenant-admin/clients'],
      headerImageUrl: '/assets/images/user.png',
      backgroundImageUrl: '/assets/images/clients_tile_background.jpg'
    },
    {
      title: 'Aktionsübersicht',
      subtitle: 'Aktionsnachverfolgung',
      content: 'Hier finden Sie eine Übersicht von Auffälligkeiten bei den nachverfolgten Index- und Kontaktfällen, ' +
        'die Ihre Aufmerksamkeit erfordern.',
      linkText: 'Zu den Aktionen',
      routerLink: ['/tenant-admin/actions'],
      headerImageUrl: '/assets/images/diary.png',
      backgroundImageUrl: '/assets/images/actions_tile_background.jpg'
    }
  ];

  constructor(private userService: UserService) {

  }

  ngOnInit(): void {
    this.tilesToShow = this.userService.isHealthDepartmentUser ? this.healthDepartmentTiles : this.clientTiles;
  }

}
