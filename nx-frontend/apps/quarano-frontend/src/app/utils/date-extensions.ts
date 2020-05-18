interface Date {
  addDays(numberOfDays: number): Date;
  getDateWithoutTime(): string;
  toCustomLocaleDateString(): string;
  isDateInPast(): boolean;
}

Date.prototype.addDays = function (numberOfDays) {
  const date = new Date(this.valueOf());
  date.setDate(date.getDate() + numberOfDays);
  return date;
};

Date.prototype.getDateWithoutTime = function () {
  const date = new Date(this.valueOf());
  return new Date(date.getTime() - date.getTimezoneOffset() * 60000).toISOString().split('T')[0];
};

Date.prototype.toCustomLocaleDateString = function () {
  const date = new Date(this.valueOf());
  return date.toLocaleDateString('de-DE', { day: '2-digit', month: '2-digit', year: 'numeric' });
};

Date.prototype.isDateInPast = function () {
  const date = new Date(this.valueOf());
  const today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  return today.getDateWithoutTime() > date.getDateWithoutTime();
};
