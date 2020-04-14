interface Date {
  addDays(numberOfDays: number): Date;
  getDateWithoutTime(): string;
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
