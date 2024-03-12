import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../models/user'; // Asegúrate de tener una clase modelo para User
import { Product } from 'src/app/models/product';
import { Shopping } from 'src/app/models/shopping';
import { Router } from '@angular/router';

import { CookieService } from 'ngx-cookie-service';
import { jwtDecode } from "jwt-decode";
import { tap } from 'rxjs/operators';
import { rentresponse } from 'src/app/models/rentResponse';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl: string = 'http://localhost:18082';

  constructor(private http: HttpClient,
    private router: Router,
    private cookieService: CookieService,
    ) { }

	getGlobalHeaders(){
		return new HttpHeaders({
	      'Content-Type': 'application/json',
	      "Accept"      : '*/*',
	      'Authorization': `Bearer ` + this.cookieService.get('Token')
	    });
	}


 // Actualizar usuario para ser administrador
  setAdmin(email: string): Observable<any> {
    const url = `${this.baseUrl}/users/setAdmin?email=${email}`
    return this.http.put(url, {}, {headers : this.getGlobalHeaders()})
  }
  // Obtener información del usuario por ID
  getUserById(userId: string): Observable<User | undefined> {
    return this.http.get<User | undefined>(`${this.baseUrl}/users/${userId}`, {headers : this.getGlobalHeaders()});
  }

  // Actualizar información del usuario
  updateUser(userData: User): Observable<any> {
    return this.http.put(`${this.baseUrl}/users/${userData.id}`, userData, {headers : this.getGlobalHeaders()}).pipe(
      tap({
        next: (response) => {
          // Llamada exitosa, ahora llama a getJWTComponent
          sessionStorage.setItem('USER',JSON.stringify(response));
        },
        error: (error) => {
          // Manejar el error si es necesario
          console.error('Error al actualizar el usuario:', error);
        }
      })
    );
  }

  getUserCart(userId: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/users/${userId}/cart`);
  }

  getProductsByUserId(userId: number): Observable<Shopping[]| undefined> {
    return this.http.get<Shopping[]>(this.baseUrl + "/orders/" + userId.toString(), {headers : this.getGlobalHeaders()});
  }

  getJTWComponent() {
    const jwtToken = this.cookieService.get('Token');
    // JWT válido
    if (jwtToken) {
      try {
        // Decodificar JWT
        const decodedToken: any = jwtDecode(jwtToken);
        if (decodedToken?.id) {
          this.getUserById(decodedToken.id).subscribe({
            next: (userData: User | undefined) => {
              if (userData) {
                sessionStorage.setItem('USER', JSON.stringify(userData));
                this.navigateToCatalog();
              } else {
                console.error('No se obtuvieron datos de usuario válidos.');
              }
            },
            error: (error: any) => {
              console.error('Error al obtener datos del usuario:', error);
            }
          });
        }
      } catch (error) {
        console.error('Error al decodificar el JWT:', error);
      }
    } else {
      console.error('No se encontró el JWT en la cookie.');
    }
  }


  // Navigate to the catalog and reload the page
  private navigateToCatalog() {
    this.router.navigateByUrl('/catalog').then(() => {
      window.location.reload();
    });
  }


  //CART AREA

  addToCart(productId: number, userId:number): Observable<Shopping> {
    const body = {productId: productId, userId: userId, quantity:1};
    return this.http.post<Shopping>(this.baseUrl+"/orders", body, {headers : this.getGlobalHeaders()});
  }

  deleteProductFromCart(rentId:number): Observable<ResponseType> {
    return this.http.delete<ResponseType>(this.baseUrl + "/orders/"+rentId.toString(), {headers : this.getGlobalHeaders()});
  }

  payCart(userId:number): Observable<ResponseType> {
    return this.http.get<ResponseType>(this.baseUrl + "/orders/cart/pay/" + userId.toString(), {headers : this.getGlobalHeaders()})
  }

  updateQuantity(rentId:number, quantity:number) {
    const body = {cartId: rentId, quantity:quantity};
    return this.http.post<rentresponse>(this.baseUrl+"/orders/cart/updateqty", body, {headers : this.getGlobalHeaders()});
  }

  isAvailableProduct(product: Product) {
    return this.http.get<rentresponse>(this.baseUrl+"/orders/isavailable/" + product.id.toString(), {headers : this.getGlobalHeaders()});
  }

  getOrdersByUserId(userId:number) {
    return this.http.get<Shopping[]>(this.baseUrl + "/orders/reservations/byuserid/" + userId.toString(), {headers : this.getGlobalHeaders()});
  }

  getProductsbyOrderId(orderId:number) {
    return this.http.get<Shopping[]>(this.baseUrl+"/orders/reservations/getByOrderId/"+orderId.toString(), {headers : this.getGlobalHeaders()});
  }

  sendChangePasswordEmail(): Observable<string> {
    const jwtToken = this.cookieService.get('Token');
    const requestBody = {
      token: jwtToken
    };
    return this.http.post<string>(`${this.baseUrl}/users/send-change-password-email`, requestBody, { headers : this.getGlobalHeaders(), responseType: 'text' as 'json' });
  }

  changePassword(newPassword: string): Observable<string> {
    const jwtToken = this.cookieService.get('Token');
    const tempCode = localStorage.getItem('TEMPCODE');
    localStorage.removeItem('TEMPCODE');
    const requestBody = {
      token: jwtToken,
      tempCode: tempCode,
      newPassword: newPassword
    };
     return this.http.post<string>(`${this.baseUrl}/users/change-password`, requestBody, { responseType: 'text' as 'json' });
  }
  changePasswordUnsafe(uuid:string, email:string, newPassword: string): Observable<string> {
    const requestBody = {
      safecode: uuid,
      email: email,
      newPassword: newPassword
    };
     return this.http.post<string>(`${this.baseUrl}/users/change-password`, requestBody, { responseType: 'text' as 'json' });
  }

  sendChangePasswordEmailUnsafe(email: string){
    return this.http.get(this.baseUrl+"/users/send-change-password-unsafe-email?email="+email.toString(), { headers : this.getGlobalHeaders(), responseType: 'text' });
  }

}
