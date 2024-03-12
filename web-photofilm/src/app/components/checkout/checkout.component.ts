import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Shopping } from 'src/app/models/shopping';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user/user.service';

declare var paypal: any; // Declara 'paypal' para acceder al SDK de PayPal

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styles: [ /* Estilos aquí si es necesario */ ]
})
export class CheckoutComponent {
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private userService: UserService,
    private router: Router

  ) {
    this.totalAmount = 0;
  }

  userData: User | undefined;
  totalAmount;

  proceedToPay() {
    // Aquí se incluirá la lógica de integración con PayPal
    if(this.userData?.id) {
      this.userService.payCart(this.userData.id).subscribe();

      this.router.navigate(['/catalog']);
    } else {
      this.router.navigate(['/login']); //if user is logout we redirect to login
    }
  }

  ngOnInit() {
    const user = sessionStorage.getItem("USER");
    if (user) {
      this.userData = JSON.parse(user);
    } else {
      this.router.navigate(['/login']); // Utiliza el Router para la redirección
    }

    this.userService.getProductsByUserId(this.userData?.id || 0).subscribe({
      next: (cartItems: Shopping[] | undefined) => {
        this.totalAmount = cartItems?.reduce((sum, item) => sum + item.product.dailyPrice * item.quantity, 0) || 0;
      },
      error: (error: any) => {
        console.error('Error al cargar los productos del carrito:', error);
      }
    }
    )
  }

  ngAfterViewInit(): void {
    paypal.Buttons({
      createOrder: (data: any, actions: any) => {
        // Configura la transacción
        return actions.order.create({
          purchase_units: [{
            amount: {
              value: '0.35' // Reemplaza con el monto total de la compra
            }
          }]
        });
      },
      onApprove: (data: any, actions: any) => {
        // Captura la transacción
        return actions.order.capture().then((details: any) => {
          this.successMessage = `Transacción completada por ${details.payer.name.given_name}.`;
          // Aquí puedes realizar acciones adicionales como actualizar tu base de datos
          //TODO llamar al back y pasarle el ID de transaccion
          if (this.userData?.id) {
            this.userService.payCart(this.userData?.id).subscribe();
          }

          this.router.navigate(['/catalog']);
        });
      },
      onError: (err: any) => {
        // Manejo de errores
        this.errorMessage = err;
        console.error(err);
      }
    }).render('#paypal-button-container');
  }
}
