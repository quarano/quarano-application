import { Component, OnInit, Input } from '@angular/core';
import { ITileViewModel } from './tile-view-model';

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

