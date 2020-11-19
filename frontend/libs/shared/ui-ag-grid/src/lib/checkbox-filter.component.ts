import { Component, ViewChild, ViewContainerRef } from '@angular/core';
import { IFilterAngularComp } from 'ag-grid-angular';
import { IAfterGuiAttachedParams, IDoesFilterPassParams, IFilterParams, RowNode } from 'ag-grid-community';

@Component({
  selector: 'qro-checkbox-filter',
  template: `
    <mat-select multiple matTooltip="Filtern" (selectionChange)="onAlertFilterChanged($event.value)">
      <mat-option *ngFor="let key of Object.keys(T)" [value]="params.node.data[key]"> </mat-option>
    </mat-select>
  `,
  styles: [
    `
      .container {
        border: 2px solid #22ff22;
        border-radius: 5px;
        background-color: #bbffbb;
        width: 200px;
        height: 50px;
      }

      input {
        height: 20px;
      }
    `,
  ],
})
export class CheckboxFilterComponent<T> implements IFilterAngularComp {
  private params: IFilterParams;
  private valueGetter: (rowNode: RowNode) => any;
  public text: string = '';

  @ViewChild('input', { read: ViewContainerRef }) public input;

  agInit(params: IFilterParams): void {
    this.params = params;
    this.valueGetter = params.valueGetter;
  }

  isFilterActive(): boolean {
    return this.text != null && this.text !== '';
  }

  doesFilterPass(params: IDoesFilterPassParams): boolean {
    return this.text
      .toLowerCase()
      .split(' ')
      .every((filterWord) => this.valueGetter(params.node).toString().toLowerCase().indexOf(filterWord) >= 0);
  }

  getModel(): any {
    return { value: this.text };
  }

  setModel(model: any): void {
    this.text = model ? model.value : '';
  }

  afterGuiAttached(params: IAfterGuiAttachedParams): void {
    window.setTimeout(() => this.input.element.nativeElement.focus());
  }

  // noinspection JSMethodCanBeStatic
  componentMethod(message: string): void {
    alert(`Alert from PartialMatchFilterComponent: ${message}`);
  }

  onChange(newValue): void {
    if (this.text !== newValue) {
      this.text = newValue;
      this.params.filterChangedCallback();
    }
  }
}
