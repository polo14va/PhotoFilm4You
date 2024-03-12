import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth/auth.service';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidatorFn } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit{
  showPassword = false;
  showPassword2 = false;
  registerForm: FormGroup;
  serverErrorMessage: string | null = null;

  constructor(private auth: AuthService, private fb: FormBuilder,private router: Router) {
    this.registerForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(5), this.nameValidator]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordValidator]],
      password2: ['', Validators.required],
      phoneNumber: ['', [Validators.required, this.phoneNumberValidator()]],
    }, { validators: this.passwordMatchValidator },
    );

  }

  ngOnInit() {}

  onSubmit() {
    if (this.registerForm.valid) {
      const user: User = this.registerForm.value;
      this.registerUser(user);
    }
  }

  nameValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const name = control.value;
    const regex = /^[a-zA-Z]+( [a-zA-Z]+)*$/;
    const valid = regex.test(name);
    return valid ? null : { 'pattern': true };
  }

  passwordValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const password = control.value;
    const hasNumber = /\d/.test(password);
    const hasUpper = /[A-Z]/.test(password);
    const hasLower = /[a-z]/.test(password);

    const valid = hasNumber && hasUpper && hasLower;

    return valid ? null : { 'pattern': true };
  }

  passwordMatchValidator(group: FormGroup): { [key: string]: boolean } | null {
    const password = group.get('password')?.value;
    const password2 = group.get('password2')?.value;

    return password === password2 ? null : { 'passwordMismatch': true };
  }

  phoneNumberValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const phoneNumberPattern = /^(\+34|0034|34)?[ -]*(6|7|8|9)[ -]*([0-9][ -]*){8}$/;
      const valid = phoneNumberPattern.test(control.value);
      return valid ? null : { 'phoneNumber': { value: control.value } };
    };
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
  togglePassword2Visibility() {
    this.showPassword2 = !this.showPassword2;
  }

  registerUser(user: User) {
    this.auth.registerUser(user).subscribe(
      (response) => {
        // Registro OK
        alert('Usuario registrado correctamente');
        // Convierte la respuesta a cadena de texto
        const responseText = JSON.stringify(response);

        // Extrae el número del mensaje
        const numeroExtraido = responseText.match(/\d+/);

        if (numeroExtraido) {
          const idUsuario = numeroExtraido[0];
          // Cambia localStorage por sessionStorage
          sessionStorage.setItem("USER", idUsuario);
          this.router.navigate(['/login']);
        }
      },
      (error) => {
        console.error('Error al registrar el usuario:', error);
        if (error.status === 400) {
          // Handle bad request errors (validation errors)
          this.serverErrorMessage = 'Error en el registro, alguno de los datos introducidos no es válido. o ya existe un usuario con ese email.';
        } else {
          // Handle other server-side errors
          this.serverErrorMessage = 'Error insesperado, por favor recargue la página e inténtelo de nuevo.';
        }


      }
    );
  }
}
