import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ReactiveFormsModule } from '@angular/forms';
import { Router,ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/services/user/user.service';


@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './change-password.component.html'
})
export class ChangePasswordComponent implements OnInit {
  passwordForm: FormGroup;
  showPassword = false;
  static passwordCriteria = [
    { check: (value: string) => /\d/.test(value), text: 'At least one number' },
    { check: (value: string) => /[A-Z]/.test(value), text: 'At least one uppercase letter' },
    { check: (value: string) => /[a-z]/.test(value), text: 'At least one lowercase letter' },
    { check: (value: string) => value.length >= 8, text: 'At least 8 characters' }
  ];
  passwordCriteriaInstance = ChangePasswordComponent.passwordCriteria;
  passwordSafe: boolean = false;
  uuid: string = '';
  email: string = '';


  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserService,
    private fb: FormBuilder) {
    this.passwordForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(8), this.passwordValidator]]
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const tempCode = params['TEMPCODE'];
      this.uuid = params['SAFECODE'];
      this.email = params['EMAIL'];
      if (tempCode) {
        localStorage.setItem('TEMPCODE', tempCode);
        this.router.navigate(['user/change-password']);
      }
    });
  }

  passwordValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const password = control.value;
    const valid = ChangePasswordComponent.passwordCriteria.every(criteria => criteria.check(password));
    return valid ? null : { 'pattern': true };
  }


  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  changePassword(): void {
    if (this.passwordForm.valid) {

      if (localStorage.getItem('TEMPCODE')) {
        this.passwordSafe = true;
      }

      if (this.passwordSafe ){
        
      // Logic to change password safe
        this.userService.changePassword(this.passwordForm.value.newPassword).subscribe({
          next: (response: any) => {
            
            this.router.navigate(['/login']);
            alert('Contraseña cambiada, vuelva a inciar sesión');
          },
          error: (error: any) => {
            console.error('Error al cambiar la contraseña:', error);
            this.passwordForm.reset();
          }
        });

      }
      else{
        
        this.userService.changePasswordUnsafe(this.uuid, this.email, this.passwordForm.value.newPassword).subscribe({
          next: (response: any) => {
            
            this.router.navigate(['/login']);
            alert('Contraseña cambiada, vuelva a inciar sesión');
          },
          error: (error: any) => {
            console.error('Error al cambiar la contraseña:', error);
            this.passwordForm.reset();
          }
        });

      }
    }
  }


}
