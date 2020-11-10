import { HalResponse } from '@qro/shared/util-data-access';
import { FormControl } from '@angular/forms';
import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, filter, startWith, takeUntil, tap } from 'rxjs/operators';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { COMMA, ENTER } from '@angular/cdk/keycodes';

@Component({
  selector: 'qro-lazy-autocomplete',
  templateUrl: './lazy-autocomplete.component.html',
  styleUrls: ['./lazy-autocomplete.component.scss'],
})
export class LazyAutocompleteComponent implements OnInit, OnDestroy {
  @Input() control: FormControl;
  @Input() placeholder: string;
  private _selectableItems: HalResponse[] = [];
  @Input() get selectableItems(): HalResponse[] {
    return this._selectableItems;
  }

  set selectableItems(val: HalResponse[]) {
    this._selectableItems = val;
    if (typeof this.searchTerm === 'string') {
      this._filter(this.searchTerm);
    }
  }

  @Input() displayWith: ((value: HalResponse) => string) | null;
  @Output() removed = new EventEmitter<HalResponse>();
  @Output() added = new EventEmitter<HalResponse>();
  @Output() itemNotFound = new EventEmitter<string>();
  @Output() completeMethod: EventEmitter<string> = new EventEmitter();
  selectedItems = [];
  inputControl = new FormControl();
  separatorKeysCodes: number[] = [ENTER, COMMA];
  @ViewChild('input') input: ElementRef<HTMLInputElement>;
  @ViewChild('auto') autocomplete;
  destroy$: Subject<void> = new Subject<void>();
  filteredList$$: BehaviorSubject<HalResponse[]> = new BehaviorSubject<HalResponse[]>([]);
  private searchTerm = '';
  items = [];

  ngOnInit() {
    this.filteredList$$.next(this.selectableItems);
    this.control.valueChanges
      .pipe(
        takeUntil(this.destroy$),
        filter((data) => !!data)
      )
      .subscribe((data: HalResponse[]) => {
        this.selectableItems = data;
        this.control.markAsDirty();
        data.forEach((value) => this.added.emit(value));
      });

    this.initializePreselctedItems();

    this.inputControl.valueChanges
      .pipe(
        takeUntil(this.destroy$),
        debounceTime(500),
        distinctUntilChanged(),
        startWith(null as string),
        tap((searchTerm) => (this.searchTerm = searchTerm))
      )
      .subscribe((searchTerm) => {
        if (typeof searchTerm === 'string') {
          this.completeMethod.emit(searchTerm);
          this._filter(searchTerm);
        }
      });
  }

  private initializePreselctedItems(): void {
    const preselected = this.control.value;
    if (preselected instanceof Array) {
      preselected.forEach((item) => this.selectedItems.push(item));
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get prefilteredList(): HalResponse[] {
    return this.selectableItems.reduce((total, item) => {
      if (!this.selectedItems.find((i) => i._links.self.href === item._links.self.href)) {
        total.push(item);
      }
      return total;
    }, []);
  }

  checkInputForData() {
    const input = this.inputControl.value;
    const inList = this.isInputInSelectedList(input);
    if (!inList && input) {
      this.itemNotFound.emit(input);
    }
  }

  private isInputInSelectedList(searchString: string): any {
    return this.selectableItems.find((item) => this.displayWith(item) === searchString);
  }

  private _filter(searchTerm: string) {
    if (this.displayWith) {
      let arrayToReturn = this.prefilteredList.filter(
        (item) => !this.selectedItems.find((i) => i._links.self.href === item._links.self.href)
      );

      if (!searchTerm) {
        this.filteredList$$.next(this.prefilteredList);
      }
      const filterValue = searchTerm.toLowerCase();
      arrayToReturn = arrayToReturn.filter((item) => this.displayWith(item).toLowerCase().indexOf(filterValue) === 0);
      this.filteredList$$.next(arrayToReturn);
    }
  }

  get disabled(): boolean {
    return this.control.disabled;
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    const selectedValue = event.option.value;
    this.selectedItems.push(selectedValue);
    this.selectedItems.concat([selectedValue]);
    this.input.nativeElement.value = '';
    this.inputControl.setValue(null);
    this.setFormControlValue();
    this.added.emit(selectedValue);
    this.filteredList$$.next(this.prefilteredList);
  }

  remove(item: any): void {
    const index = this.selectedItems.indexOf(item);

    if (index >= 0) {
      this.selectedItems.splice(index, 1);
      this.setFormControlValue();
      this.removed.emit(item);
    }
    this.filteredList$$.next(this.prefilteredList);
  }

  setFormControlValue() {
    this.control.setValue(this.selectedItems);
    this.control.markAsDirty();
  }

  getNameById(id: string) {
    const item = this.selectableItems.find((i) => i._links.self.href === id);
    if (item) {
      return this.displayWith(item);
    }
    return '';
  }
}
