import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrdersProductDetailsComponent } from './orders-product-details.component';

describe('OrdersProductDetailsComponent', () => {
  let component: OrdersProductDetailsComponent;
  let fixture: ComponentFixture<OrdersProductDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OrdersProductDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrdersProductDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
