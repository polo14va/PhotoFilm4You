import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OrdersComponent } from './orders.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { User } from 'src/app/models/user';
import { UserService } from '../../services/user/user.service';
import { Shopping } from 'src/app/models/shopping';
import { CookieService } from 'ngx-cookie-service';

class MockActivatedRoute {
  params = of({ id: '1' });
}

class MockUserService {
  // Puedes agregar métodos simulados según sea necesario para tus pruebas
  getOrdersByUserId(userId: number) {
    // Simula la respuesta del servicio
    const shoppingItems: Shopping[] = [
      // ... Agrega elementos simulados según tus necesidades ...
    ];
    return of(shoppingItems);
  }
}

describe('OrdersComponent', () => {
  let component: OrdersComponent;
  let fixture: ComponentFixture<OrdersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OrdersComponent],
      providers: [
        // Agrega el proveedor para ActivatedRoute con el Mock
        { provide: ActivatedRoute, useClass: MockActivatedRoute },
        // Agrega el proveedor para UserService con el Mock
        { provide: UserService, useClass: MockUserService },
        // Puedes agregar más proveedores simulados según sea necesario
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(OrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
