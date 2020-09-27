import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminIncomeEmailsComponent } from './admin-income-emails.component';

describe('AdminIncomeEmailsComponent', () => {
  let component: AdminIncomeEmailsComponent;
  let fixture: ComponentFixture<AdminIncomeEmailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminIncomeEmailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminIncomeEmailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
