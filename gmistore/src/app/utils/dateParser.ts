export function parseDate(date: Date) {
  let fullYear = date.getFullYear();
  let month = date.getMonth();
  let day = date.getDay();
  let hours = date.getHours();
  let minutes = date.getMinutes();
  return fullYear + '.' + month + '.' + day + '. ' + hours + ':' + minutes;
}
