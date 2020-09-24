export function generateRandomColor(arr: Array<any>) {
  let letters = '0123456789ABCDEF'.split('');
  let color = '#E6'; //transparency 90%
  let colors = [];
  for (let j = 0; j < arr.length; j++) {
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    colors.push(color)
    color = '#E6';
  }
  return colors;
}
