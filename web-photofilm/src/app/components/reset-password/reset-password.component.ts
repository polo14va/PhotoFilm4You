import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user/user.service';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html'
})
export class ResetPasswordComponent implements OnInit {
  resetPasswordForm: FormGroup = this.fb.group({});
  emailSent: boolean = false;
  isLoggedIn: boolean = false;

  constructor(private fb: FormBuilder, private userService: UserService,
    private cookieService: CookieService,
    private router: Router,
    ) {}

  ngOnInit() {
    this.resetPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }


  onSubmit() {
    if (this.resetPasswordForm.valid) {
      const email = this.resetPasswordForm.value.email;
      this.userService.sendChangePasswordEmailUnsafe(email).subscribe({
        next: (response) => {
          
          // Manejo de respuesta exitosa
          alert("Revisa tu correo");
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error(err);
          // Manejo de error
          // Puedes personalizar este mensaje en función del error recibido
          alert("Revisa tu correo");
        }
      });
    }
  }

    // Handle user logout
    logout() {
      this.cookieService.delete('Token');
    }

}
