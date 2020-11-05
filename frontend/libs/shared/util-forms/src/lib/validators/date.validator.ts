import { Moment } from 'moment';
import { FormGroup, ValidatorFn } from '@angular/forms';

export const DateOrderValidator: ValidatorFn = (fg: FormGroup) => {
  const end = fg.get('end')?.value as Moment;
  const start = fg.get('start')?.value as Moment;

  if (start && end) {
    if (start.diff(end, 'days') > 0) {
      return { dateOrder: true };
    }
  }

  return null;
};
