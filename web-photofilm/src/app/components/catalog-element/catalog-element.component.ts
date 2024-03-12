import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Product } from 'src/app/models/product';
import { User } from '../../models/user';
import { Alert } from 'src/app/models/Alert';
import { CookieService } from 'ngx-cookie-service';
import { ProductService } from 'src/app/services/product/product.service';
import { UserService } from 'src/app/services/user/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-catalog-element',
  templateUrl: './catalog-element.component.html',
})
export class CatalogElementComponent implements OnInit {
  @Input() product: Product | undefined;
  @Input() produtAlert: Alert[] | undefined;

  @Output() addAlertEvent = new EventEmitter<Product>();
  @Output() addToCartEvent = new EventEmitter<Product>();

  isLoggedIn: boolean = false;
  private user?: User;

  constructor(
    private cookieService: CookieService,
    private userService : UserService,
    private router: Router,
  ) {}

  ngOnInit() {
    const userFromStorage = sessionStorage.getItem('USER');
    if (userFromStorage) {
      this.user = JSON.parse(userFromStorage);
    }
  }

  hasProduct(): boolean {
    return !!this.product;
  }

  addProductToAlerts() {
    this.addAlertEvent.emit(this.product); // Emitir el evento con el producto
  }

  hasAlertForProduct(productId: number): boolean {
    if (productId !== undefined && this.produtAlert !== undefined) {
      return this.produtAlert.some(
        (alert) => Number(alert.productId) === productId
      );
    }
    return false;
  }

  getImagePath(): string {
    if (this.product && this.product.imageName) {
      return `assets/images/${this.product.imageName}`;
    } else {
      return 'assets/images/notfound.svg';
    }
  }

  getProductName(): string {
    if (this.product && this.product.name) return this.product.name;
    else return 'not found';
  }

  isButtonDisabled(): boolean {
    return this.user === null || this.user === undefined;
  }

  addToCart(event: Event){
    if (this.product == undefined) {
      return ;
    }
    this.userService.isAvailableProduct(this.product).subscribe(
        response => {
          if (response.message.indexOf("Error") != -1) {
  
            window.alert("Error: No hay stock");
          }
        },
        (error) => {
          console.error("Error de servidor", error);

        }
      )
      this.addToCartEvent.emit(this.product); // Emit add to cart event with product;
      
  }

}
