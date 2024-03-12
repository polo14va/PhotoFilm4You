import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CookieService } from 'ngx-cookie-service';
import { UserService } from 'src/app/services/user/user.service';
import { LoginModalComponent } from '../login-modal/login-modal.component';
import { LoginResponse } from './loginInterface';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styles: []
})
export class LoginComponent implements OnInit {
  isLoggedIn: boolean = false;
  @Output() loginSuccess = new EventEmitter<void>();

  loginForm!: FormGroup;
  loginError: string = '';
  emailSent: boolean = false;


  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private loginModal: LoginModalComponent,
    private cookieService: CookieService,
    private userService: UserService


  ) {
    this.createLoginForm();
  }

  ngOnInit() {
    this.checkUserSession();
  }

  // Create the login form with validators
  private createLoginForm() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  // Check if a user session already exists
  private checkUserSession() {
    const userSession = this.cookieService.get('USER');
    if (userSession) {
      this.isLoggedIn = true;
      this.loginSuccess.emit();
    }
  }

  // Handle user login
  loginUser(): void {
    if (this.loginForm?.valid) {
      this.auth.login(this.loginForm.value).subscribe({
        next: httpResponse => this.handleLoginResponse(httpResponse),
        error: error => this.handleLoginError(error)
      });
    }
  }

  // Handle login response
  private handleLoginResponse(httpResponse: any): void {
    const response = httpResponse.body;
    if (httpResponse.status === 200) {
      this.storeTokenAndNavigate(response);
    } else {
      this.handleUnexpectedResponse(response);
    }
  }

  // Store the JWT token and navigate to the catalog
  private storeTokenAndNavigate(token: string) {
    this.cookieService.set('Token', token);
    this.userService.getJTWComponent();
    this.loginModal.closeModal();
    this.loginSuccess.emit();
  }


  // Handle unexpected login response
  private handleUnexpectedResponse(response: LoginResponse | null) {
    alert('Error logging in: ' + (response?.error ?? 'Unknown error'));
    this.loginError = 'Unexpected error. Please try again.';
    this.resetForm();
  }

  // Handle login error
  private handleLoginError(error: { status: number; }) {
    if (error.status === 401) {
        this.loginError = 'Incorrect credentials. Please try again.';
    } else if (error.status === 403) {
        this.loginError = 'Account not validated. Please check your email for activation.';
    } else {
        this.loginError = 'Error connecting to the server.';
    }
    this.resetForm();
}

  // Reset the login form
  resetForm() {
    this.loginForm?.get('password')?.reset();
  }

  // Handle user logout
  logout() {
    this.cookieService.delete('Token');
    this.isLoggedIn = false;
    this.loginModal.closeModal();
  }


  resendConfirmation(): void {
    const emailValue = this.loginForm.get('email')?.value;
    if (emailValue && this.loginForm.get('email')?.valid) {

      this.auth.resendEmailConfirmation(emailValue)
      .subscribe({
        next: response => {
          this.emailSent = true;
        },
        error: error => {
          this.emailSent = false;
        }
      });
    }
  }




}
