import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {
  pictureUris = new Map<string, string>([
    ['https://www.intersoft.de/', '../../../assets/sponsored_by_intersoft.png'],
    ['https://wirvsvirushackathon.org/', '../../../assets/Logo_Projekt_01_weiss.png'],
    ['https://opensource.org/', '../../../assets/open_source.png'],
  ]);
}
