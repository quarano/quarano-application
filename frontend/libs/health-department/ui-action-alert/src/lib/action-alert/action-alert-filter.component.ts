import { Component } from '@angular/core';
import { Alert, getAlertConfigurations } from '@qro/health-department/domain';
import { IFilterAngularComp } from 'ag-grid-angular';
import { IDoesFilterPassParams, IFilterParams, RowNode } from 'ag-grid-community';
import { uniq, flatten, intersection } from 'lodash';

@Component({
  selector: 'qro-action-alert-filter',
  template: `
    <section class="example-section">
      <span class="example-list-section">
        <ul>
          <li *ngFor="let option of options">
            <mat-checkbox [(ngModel)]="option.selected" (ngModelChange)="onChange()">
              {{ alertConfigurationFor(option.label).displayName }}
            </mat-checkbox>
          </li>
        </ul>
      </span>
      <button mat-stroked-button (click)="selectAll()" *ngIf="hasUnselectedOptions()">Alle</button>
      <button mat-stroked-button (click)="deselectAll()" *ngIf="areAllOptionsSelected()">Alle abwählen</button>
    </section>
  `,
  styles: [
    `
      .example-section {
        margin: 12px 0;
      }

      .example-margin {
        margin: 0 12px;
      }

      ul {
        list-style-type: none;
        margin: 0;
        padding: 0 15px;
      }

      button {
        margin-left: 15px;
        margin-top: 10px;
      }
    `,
  ],
})
export class ActionAlertFilterComponent implements IFilterAngularComp {
  private params: IFilterParams;
  private valueGetter: (rowNode: RowNode) => any;
  public options: { label: string; selected: boolean }[] = [];

  agInit(params: IFilterParams): void {
    this.params = params;
    this.valueGetter = params.valueGetter;
    this.initOptions();
  }

  private initOptions() {
    const field = this.params.colDef.field;
    // @ts-ignore
    this.options = uniq(flatten(this.params.rowModel.rowsToDisplay.map((d) => d.data[field])))
      .sort(function (a: string, b: string) {
        return a.toLowerCase().localeCompare(b.toLowerCase());
      })
      .map((o) => ({
        label: o,
        selected: true,
      }));
  }

  isFilterActive(): boolean {
    if (!this.options) {
      return false;
    }
    return this.hasUnselectedOptions();
  }

  hasUnselectedOptions(): boolean {
    return this.options.map((o) => o.selected).includes(false);
  }

  areAllOptionsSelected(): boolean {
    return this.options.every((o) => !!o.selected);
  }

  doesFilterPass(params: IDoesFilterPassParams): boolean {
    const selectedOptions = this.options.filter((o) => o.selected).map((o) => o.label.toLowerCase());

    const optionsInNode = this.valueGetter(params.node).map((e) => e.toLowerCase());

    return intersection(selectedOptions, optionsInNode).length > 0;
  }

  getModel(): any {
    return this.options;
  }

  setModel(model: any): void {
    if (model) {
      model.forEach((val) => {
        const index = this.options.map((o) => o.label).indexOf(val.label);
        if (index > -1) {
          this.options[index] = val;
        }
      });
    } else {
      this.options = [];
    }
  }

  selectAll() {
    this.options.forEach((o) => (o.selected = true));
    this.onChange();
  }

  deselectAll() {
    this.options.forEach((o) => (o.selected = false));
    this.onChange();
  }

  onChange(): void {
    this.params.filterChangedCallback();
  }

  alertConfigurationFor(alert: Alert) {
    return getAlertConfigurations().find((c) => c.alert === alert);
  }
}
