import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IncomeSpentGraphComponent } from './income-spent-graph.component';

describe('IncomeSpentGraphComponent', () => {
  let component: IncomeSpentGraphComponent;
  let fixture: ComponentFixture<IncomeSpentGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IncomeSpentGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IncomeSpentGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
