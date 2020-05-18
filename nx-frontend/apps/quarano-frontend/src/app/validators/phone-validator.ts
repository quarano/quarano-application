import { ValidatorFn, FormGroup } from '@angular/forms';

export const PhoneOrMobilePhoneValidator: ValidatorFn = (fg: FormGroup) => {
  const phone = fg.get('phone')?.value;
  const mobilePhone = fg.get('mobilePhone')?.value;
  return phone || mobilePhone ? null : { phoneMissing: true };
};
