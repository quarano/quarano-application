import { Component } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';

@Component({
  selector: 'qro-email-button',
  template: `
    <button
      *ngIf="params.value"
      class="ngx-icon-button"
      data-cy="mail-button"
      mat-icon-button
      [matTooltip]="'E-Mail an ' + params.value"
      matTooltipClass="qro-tooltip"
      matTooltipPosition="right"
      (click)="sendMail($event, params.value)"
    >
      <mat-icon>mail_outline</mat-icon>
    </button>
  `,
  styles: [
    `
      .ngx-icon-button:hover,
      .ngx-icon-button:focus {
        background-color: #c3c3c3;
        color: white;
        cursor: pointer;
      }
    `,
  ],
})
export class EmailButtonComponent implements ICellRendererAngularComp {
  public params: any;

  agInit(params: any): void {
    this.params = params;
  }

  refresh(): boolean {
    return false;
  }

  sendMail(event, to: string) {
    event.stopPropagation();
    window.open(`mailto:${to}`);
  }
}
