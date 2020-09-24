export function parseDate(date: Date) {
  const full_date = new Date(date).toLocaleDateString('en-ZA');
  const full_time = new Date(date).toLocaleTimeString('hu-HU');

  return full_date + ', ' + full_time;
}
