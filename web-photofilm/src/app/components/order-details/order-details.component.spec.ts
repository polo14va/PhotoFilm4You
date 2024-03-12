import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router'; // Importa ActivatedRoute
import { of } from 'rxjs'; // Importa of desde rxjs
import { OrderDetailsComponent } from './order-details.component';

describe('OrderDetailsComponent', () => {
  let component: OrderDetailsComponent;
  let fixture: ComponentFixture<OrderDetailsComponent>;

  // Define un mock para ActivatedRoute
  const activatedRouteMock = {
    params: of({ id: 1 }) // Puedes ajustar los valores según tu necesidad
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OrderDetailsComponent],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock } // Proporciona ActivatedRouteMock como un valor
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OrderDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create OrderDetails component', () => {
    expect(component).toBeTruthy();
  });
});
