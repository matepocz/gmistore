import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {animate, AnimationEvent, state, style, transition, trigger} from '@angular/animations';

export type FadeState = 'visible' | 'hidden';

@Component({
  selector: 'app-product-search',
  templateUrl: './product-search.component.html',
  styleUrls: ['./product-search.component.css'],
  animations: [
    trigger('state', [
      state(
        'visible',
        style({
          opacity: '1'
        })
      ),
      state(
        'hidden',
        style({
          opacity: '0'
        })
      ),
      transition('* => visible', [animate('500ms ease-out')]),
      transition('visible => hidden', [animate('500ms ease-out')])
    ])
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class ProductSearchComponent implements OnInit {

  state: FadeState;
  // tslint:disable-next-line: variable-name
  private _show: boolean;
  get show() {
    return this._show;
  }

  constructor() {
  }

  ngOnInit(): void {
  }

  @Input()
  set show(value: boolean) {
    if (value) {
      this._show = value;
      this.state = 'visible';
    } else {
      this.state = 'hidden';
    }
  }

  animationDone(event: AnimationEvent) {
    if (event.fromState === 'visible' && event.toState === 'hidden') {
      this._show = false;
    }
  }

}
