import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserRegistrationsGraphComponent } from './user-registrations-graph.component';

describe('UserRegistrationsGraphComponent', () => {
  let component: UserRegistrationsGraphComponent;
  let fixture: ComponentFixture<UserRegistrationsGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserRegistrationsGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserRegistrationsGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
