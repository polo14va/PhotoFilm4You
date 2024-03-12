import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { UserService } from '../../services/user/user.service';
import { Shopping } from 'src/app/models/shopping';
import { CookieService } from 'ngx-cookie-service';


@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html'
})
export class OrdersComponent implements OnInit {

  orders : any[] | undefined;
  userData : User | undefined;

  constructor(
    private x: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private cookieService: CookieService
  ) {}

  ngOnInit(): void {
    const user = sessionStorage.getItem('USER');
    if (user) {
      this.userData = JSON.parse(user);
      this.loadUserOrders();
    } else {
      this.router.navigate(['/login']); // Utiliza el Router para la redirección
    }
  }

  loadUserOrders() {
    if (this.userData && this.userData.id) {
      this.userService.getOrdersByUserId(this.userData.id).subscribe({
        next: (cartItems: Shopping[] | undefined) => {
          if (cartItems) {
            let filteredOrders: { orderId: number, count: number, price: number }[] = [];

            filteredOrders = cartItems.reduce((acc: any, currentItem: Shopping) => {
              const existingOrder = acc.find((order: any) => order.orderId === currentItem.orderId);
              if (existingOrder) {
                existingOrder.count++;
                existingOrder.price += currentItem.price;
              } else {
                acc.push({ orderId: currentItem.orderId, count: 1, price: currentItem.price });
              }

              return acc;
            }, []);

            this.orders = filteredOrders;
          }
        },
        error: (error: any) => {
          console.error('Error al cargar los productos del carrito:', error);
        }
      });
    }
  }


}
