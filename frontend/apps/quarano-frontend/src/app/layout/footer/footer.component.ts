import { Component } from '@angular/core';
import { VersionDto, VersionService } from '@qro/general/api';
import { Observable } from 'rxjs';

@Component({
  selector: 'qro-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent {
  pictureUris = new Map<string, string>([
    ['https://beyond-crisis.de/quarano-e-v', 'assets/images/beyondcrisis.png'],
    [
      'https://telekomhilft.telekom.de/t5/Unsere-Startups/quarano/ba-p/4651421/jump-to/first-unread-message',
      'assets/images/Logo_TECHBOOST_Startup.jpg',
    ],
    ['https://opensource.org/', 'assets/images/open_source.png'],
    ['https://wirvsvirus.org/solution-builder/', 'assets/images/Solution-Builder.png'],
    [
      'https://www.bmbf.de/de/uebersicht-der-bmbf-gefoerderten-projekte-aus-dem-hackathon-11634.html',
      'assets/images/BMBF.jpg',
    ],
  ]);
  version$: Observable<VersionDto>;

  constructor(private versionService: VersionService) {}

  loadVersion() {
    this.version$ = this.versionService.getVersion();
  }
}
