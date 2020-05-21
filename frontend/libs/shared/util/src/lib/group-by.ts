export function groupBy<T>(list: T[], keyGetter: (n: T) => string): Map<string, T[]> {
  const group = new Map<string, T[]>();
  list.forEach((item) => {
    const key = keyGetter(item);
    const collection = group.get(key);
    if (!collection) {
      group.set(key, [item]);
    } else {
      collection.push(item);
    }
  });
  return group;
}
