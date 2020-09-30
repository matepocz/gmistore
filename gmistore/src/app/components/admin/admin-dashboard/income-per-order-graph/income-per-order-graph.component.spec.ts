import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IncomePerOrderGraphComponent } from './income-per-order-graph.component';

describe('IncomePerOrderGraphComponent', () => {
  let component: IncomePerOrderGraphComponent;
  let fixture: ComponentFixture<IncomePerOrderGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IncomePerOrderGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IncomePerOrderGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
