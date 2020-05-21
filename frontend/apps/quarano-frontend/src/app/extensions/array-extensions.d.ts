interface Array<T> {
  remove(elem: T): Array<T>;
}

Array.prototype.remove = function <T>(elem: T): T[] {
  return this.filter((e: T) => e !== elem);
};
