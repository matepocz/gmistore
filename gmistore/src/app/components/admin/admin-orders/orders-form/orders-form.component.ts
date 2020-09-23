import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-orders-form',
  templateUrl: './orders-form.component.html',
  styleUrls: ['./orders-form.component.css']
})
export class OrdersFormComponent implements OnInit {
  id: string;

  constructor(private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    (this.activatedRoute.paramMap.subscribe(
      paramMap => {
        const editableId: string = paramMap.get('id');
        if (editableId) {
          this.id = editableId;
        }
      },
      error => console.warn(error),
    ));
  }



}
