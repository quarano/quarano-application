import { Component, OnInit, Input } from '@angular/core';

export interface ITileViewModel {
  headerImageUrl: string;
  backgroundImageUrl: string;
  title: string;
  subtitle: string;
  content: string;
  linkText: string;
  routerLink: string[];
}

@Component({
  selector: 'qro-tile',
  templateUrl: './tile.component.html',
  styleUrls: ['./tile.component.scss']
})
export class TileComponent implements OnInit {
  @Input() viewModel: ITileViewModel;

  constructor() { }

  ngOnInit() {
  }

  getHeaderImageUrl() {
    return `url('${this.viewModel.headerImageUrl}')`;
  }
}

