import {Component, Input, OnInit, Renderer2} from '@angular/core';

@Component({
  selector: 'app-mini-card-data',
  templateUrl: './mini-card-data.component.html',
  styleUrls: ['./mini-card-data.component.css']
})
export class MiniCardDataComponent implements OnInit {
  @Input() backgroundColor;
  @Input() title;
  @Input() subtitle;
  @Input() icon;
  @Input() data;
  @Input() iconColor;
  @Input() noBackgroundColor = false;
  @Input() avatar;
  @Input() avatarColor;

  constructor() { }

  ngOnInit(): void {
  }



}
