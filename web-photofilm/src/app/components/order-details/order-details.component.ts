import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { Shopping } from 'src/app/models/shopping';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrl: './order-details.component.css'
})
export class OrderDetailsComponent implements OnInit  {

  orderProducts: Shopping[] | undefined;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private cookieService: CookieService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      const orderId = +params['id'];
      if (orderId) {
        this.loadOrderDetails(orderId);
      }
    });

  }

  loadOrderDetails(orderId:number) {
    if(orderId) {
      this.userService.getProductsbyOrderId(orderId).subscribe({
        next: (cartItems: Shopping[] | undefined) => {
          this.orderProducts = cartItems;
        },
        error: (error: any) => {
          console.error('Error al cargar los productos del carrito:', error);
        }
      });
    
    }
  }

  getTotalAmount(): number {
    return this.orderProducts?.reduce((sum, item) => sum + item.product.dailyPrice * item.quantity, 0) || 0;
  }

}
