import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  navigation: {label: string, href:string}[]=[
    {label:'Home',href: ''},
    {label:'Login',href: 'login'},
    {label:'Registration',href: 'register'},
  ];

  constructor() { }
}
