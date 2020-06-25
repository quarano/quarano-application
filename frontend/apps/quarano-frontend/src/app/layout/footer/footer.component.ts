import { Component } from '@angular/core';

@Component({
  selector: 'qro-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent {
  pictureUris = new Map<string, string>([
    ['https://www.intersoft.de/', 'assets/images/sponsored_by_intersoft.png'],
    ['https://wirvsvirushackathon.org/', 'assets/images/Logo_Projekt_01_weiss.png'],
    ['https://opensource.org/', 'assets/images/open_source.png'],
  ]);
}
