import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrdersProductListComponent } from './orders-product-list.component';

describe('OrdersProductListComponent', () => {
  let component: OrdersProductListComponent;
  let fixture: ComponentFixture<OrdersProductListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OrdersProductListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrdersProductListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
