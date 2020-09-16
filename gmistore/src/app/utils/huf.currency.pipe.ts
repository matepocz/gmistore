import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'hufCurrency',
})

export class HufCurrencyPipe implements PipeTransform {
  transform(
    value: number
  ): string | null {
    return new Intl.NumberFormat(
      'hu-HU', {style: 'currency', currency: 'HUF', maximumFractionDigits: 0, minimumFractionDigits: 0})
      .format(value);
  }
}
