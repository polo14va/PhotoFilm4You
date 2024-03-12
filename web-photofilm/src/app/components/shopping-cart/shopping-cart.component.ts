import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { UserService } from '../../services/user/user.service';
import { Shopping } from 'src/app/models/shopping';
import { CookieService } from 'ngx-cookie-service';
import { tap, catchError } from 'rxjs/operators';


@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {
  cartProducts: Shopping[] | undefined;
  userData: User | undefined;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private cookieService: CookieService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      const productId = +params['id'];
      if (productId) {
        this.loadUserCart();
      }
    });
    const user = sessionStorage.getItem('USER');
    if (user) {
      this.userData = JSON.parse(user);
      this.loadUserCart();
    } else {
      this.router.navigate(['/login']); // Utiliza el Router para la redirección
    }
  }

  private reloadComponent() {
    const currentRoute = this.router.url;
    this.router.navigateByUrl('./', { skipLocationChange: true }).then(() => {
      this.router.navigate([currentRoute]);
    });
  }

  loadUserCart() {
    if (this.userData && this.userData.id) {
      this.userService.getProductsByUserId(this.userData.id).subscribe({
        next: (cartItems: Shopping[] | undefined) => {
          this.cartProducts = cartItems;
        },
        error: (error: any) => {
          console.error('Error al cargar los productos del carrito:', error);
        }
      });
    }
  }

  getTotalAmount(): number {
    return this.cartProducts?.reduce((sum, item) => sum + item.product.dailyPrice * item.quantity, 0) || 0;
  }

  proceedToCheckout() {
    this.router.navigate(['/checkout']);
  }

  deleteProductFromCart(rentId:number) {
    this.userService.deleteProductFromCart(rentId)
    .pipe(
      tap(response => {
        this.loadUserCart();
      }),
      catchError(error => {
        throw error; // Re-throw the error to propagate it to the next error handler
      })
    )
    .subscribe();
  }

  updateQty(rentId:number, quantity:string) {
    if(quantity && parseInt(quantity) > 0 ) {
      this.userService.updateQuantity(rentId, parseInt(quantity)).pipe(
        tap(response => {
          if(response.message.indexOf("Error") == -1) {
            this.loadUserCart();
          } else {
            window.alert("Error cantidad insuficiente en stock");
          }
        }),
        catchError(error => {
          console.error('Error al actualizar cantidad:', error);
          throw error; // Re-throw the error to propagate it to the next error handler
        })
      ).subscribe();
    } else {
      console.error("Cantidad ha de ser mayor que 0");
      this.loadUserCart();
    }
  }

}
