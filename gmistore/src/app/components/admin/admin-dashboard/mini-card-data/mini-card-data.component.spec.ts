import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MiniCardDataComponent } from './mini-card-data.component';

describe('MiniCardDataComponent', () => {
  let component: MiniCardDataComponent;
  let fixture: ComponentFixture<MiniCardDataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MiniCardDataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MiniCardDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
