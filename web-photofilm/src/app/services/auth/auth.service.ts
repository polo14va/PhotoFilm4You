import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from 'src/app/models/user';
import { Product } from 'src/app/models/product';
import { LoginResponse, LoginInterface } from 'src/app/components/login/loginInterface';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl: string = 'http://localhost:18082/';

  constructor(private http: HttpClient, 
				private cookieService: CookieService) { }

	getGlobalHeaders(){
		return new HttpHeaders({
	      'Content-Type': 'application/json',
	      "Accept"      : '*/*',
	      'Authorization': `Bearer ` + this.cookieService.get('Token')
	    });
	}


  // Register a new user
  registerUser(user: User): Observable<any> {
    user.role = 'USER';
    const url = `${this.baseUrl}users/create`;
    return this.http.post(url, user);
  }

  // Perform user login
  login(userLogin: LoginInterface): Observable<HttpResponse<any>> {
    const url = `${this.baseUrl}auth/login`;
    const body = {
      "email": userLogin.email,
      "password": userLogin.password
    };
    return this.http.post(url, body, {
      observe: 'response',
      responseType: 'text'
  });
  }

  // Add an alert for a product and user
  addAlert(product: Product, userId: string): Observable<ResponseType> {
    const currentDate = new Date();
    const oneMonthLater = new Date();
    oneMonthLater.setMonth(currentDate.getMonth() + 1);

    const alertData = {
      productId: product.id,
      userId: userId,
      from: currentDate.toISOString(),
      to: oneMonthLater.toISOString()
    };

    return this.http.post<ResponseType>(`${this.baseUrl}alerts`, alertData, { headers: this.getGlobalHeaders() });
  }

  // Get alerts for a user by user ID
  getAlertsByUserId(userId: number): Observable<any> {
      const currentDate = new Date();
      const fromDate = currentDate.toISOString().split('T')[0];
      currentDate.setFullYear(currentDate.getFullYear() + 1);
      const toDate = currentDate.toISOString().split('T')[0];

      // Build query parameters
      const params = new HttpParams()
        .set('fromDate', fromDate)
        .set('toDate', toDate);

      // Make the HTTP request with query parameters
      return this.http.get(`${this.baseUrl}alerts/user/${userId}`, { headers: this.getGlobalHeaders(), params });
  }

  // Get all admin user IDs
  getAllAdminUserIDs(): Observable<number[]> {
    return this.http.get<number[]>(`${this.baseUrl}auth/admins`, { headers: this.getGlobalHeaders() });
  }

  resendEmailConfirmation(email: string): Observable<string> {
    return this.http.post(`${this.baseUrl}auth/resend-confirmation`, email , { responseType: 'text' });
  }

}
