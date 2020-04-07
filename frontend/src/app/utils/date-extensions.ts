interface Date {
  addDays(numberOfDays: number): Date;
}

Date.prototype.addDays = function (numberOfDays) {
  const date = new Date(this.valueOf());
  date.setDate(date.getDate() + numberOfDays);
  return date;
};
