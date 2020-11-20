import { Component } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';

@Component({
  selector: 'qro-unordered-list',
  template: `
    <div>
      <span *ngFor="let v of params.value">{{ v }}</span>
    </div>
  `,
  styles: [
    `
      div {
        display: flex;
        flex-direction: column;
        justify-content: center;
        height: 100%;
        padding: 5px 0;
      }

      span {
        line-height: 1.2;
      }
    `,
  ],
})
export class UnorderedListComponent implements ICellRendererAngularComp {
  public params: any;

  agInit(params: any): void {
    this.params = params;
    console.log(params.value);
  }

  refresh(): boolean {
    return false;
  }
}
