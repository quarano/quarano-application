import { FormControl } from '@angular/forms';
import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { filter, startWith, takeUntil } from 'rxjs/operators';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { IIdentifiable } from '../../../../util-data-access/src/lib/models/general';
import { cloneDeep } from 'lodash';

@Component({
  selector: 'qro-multiple-autocomplete',
  templateUrl: './multiple-autocomplete.component.html',
  styleUrls: ['./multiple-autocomplete.component.scss'],
})
export class MultipleAutocompleteComponent implements OnInit, OnDestroy {
  @Input() nameProperties: string[];
  @Input() control: FormControl;
  @Input() placeholder: string;
  @Input() selectableItems: IIdentifiable[];
  @Output() removed = new EventEmitter<string>();
  @Output() added = new EventEmitter<string>();
  selectedItemIds: string[];
  inputControl = new FormControl();
  separatorKeysCodes: number[] = [ENTER, COMMA];
  @ViewChild('input') input: ElementRef<HTMLInputElement>;
  destroy$: Subject<void> = new Subject<void>();
  filteredList$$: BehaviorSubject<IIdentifiable[]> = new BehaviorSubject<IIdentifiable[]>(undefined);

  ngOnInit() {
    this.filteredList$$.next(this.selectableItems);
    this.control.valueChanges
      .pipe(
        takeUntil(this.destroy$),
        filter((data) => !!data)
      )
      .subscribe((data: string[]) => {
        this.selectedItemIds = data;
        this.control.markAsDirty();
        this.input.nativeElement.blur();
        data.forEach((value) => this.added.emit(value));
      });

    this.selectedItemIds = this.control.value;

    this.inputControl.valueChanges
      .pipe(takeUntil(this.destroy$), startWith(null as string))
      .subscribe((searchTearm) => {
        if (typeof searchTearm === 'string') {
          this._filter(searchTearm);
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get prefilteredList(): IIdentifiable[] {
    let arrayToReturn: IIdentifiable[] = cloneDeep(this.selectableItems);
    this.selectedItemIds.forEach((selectedItem) => {
      arrayToReturn = arrayToReturn.filter((item) => item.id !== selectedItem);
    });
    return arrayToReturn;
  }

  private _filter(searchTerm: string) {
    let arrayToReturn = this.prefilteredList.filter((item) => !this.selectedItemIds.includes(item.id));

    if (!searchTerm) {
      this.filteredList$$.next(this.prefilteredList);
    }
    const filterValue = searchTerm.toLowerCase();
    arrayToReturn = arrayToReturn.filter((item) => this.getName(item).toLowerCase().indexOf(filterValue) === 0);
    this.filteredList$$.next(arrayToReturn);
  }

  get disabled(): boolean {
    return this.control.disabled;
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    const selectedValue = event.option.value;
    this.selectedItemIds.push(selectedValue);
    this.input.nativeElement.value = '';
    this.inputControl.setValue(null);
    this.setFormControlValue();
    this.added.emit(selectedValue);
    this.filteredList$$.next(this.prefilteredList);
  }

  remove(id: string): void {
    const index = this.selectedItemIds.indexOf(id);

    if (index >= 0) {
      this.selectedItemIds.splice(index, 1);
      this.setFormControlValue();
      this.removed.emit(id);
    }
    this.filteredList$$.next(this.prefilteredList);
  }

  setFormControlValue() {
    this.control.setValue(this.selectedItemIds);
    this.control.markAsDirty();
  }

  getNameById(id: string) {
    const item = this.selectableItems.find((i) => i.id === id);
    return this.getName(item);
  }

  getName(item: IIdentifiable) {
    if (!item) {
      return '';
    }
    let name = '';
    this.nameProperties.forEach((prop) => {
      name += item[prop] + ' ';
    });
    return name.substring(0, name.length - 1);
  }
}
