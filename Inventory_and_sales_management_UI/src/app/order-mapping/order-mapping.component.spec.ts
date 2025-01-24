import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderMappingComponent } from './order-mapping.component';

describe('OrderMappingComponent', () => {
  let component: OrderMappingComponent;
  let fixture: ComponentFixture<OrderMappingComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrderMappingComponent]
    });
    fixture = TestBed.createComponent(OrderMappingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
