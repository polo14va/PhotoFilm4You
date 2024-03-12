import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { UserService } from '../../../services/user/user.service';


@Component({
  selector: 'app-add-admin',
  templateUrl: './add-admin.component.html'
})
export class AddAdminComponent {
  newAdminForm: FormGroup;
  emailNotFoundError: string = '';

  constructor(
    private userService:UserService, 
    private router: Router,
    private fb: FormBuilder){
      this.newAdminForm = this.fb.group({
        email: ["", Validators.required], 
      });
    }
  
  
    onSubmit(){
    if(this.newAdminForm.valid){
    
      const email = this.newAdminForm.get("email")?.value;
      
      this.userService.setAdmin(email).subscribe(
        (response) =>{
          this.emailNotFoundError = '';
          alert("Usuario actualizado con exito")
        },
        (error) =>{
          console.error("Error al actualizar el usuario")
          this.emailNotFoundError = 'Error al actualizar el usuario';
        }
        
      );
    } else{
      console.warn('Email is null or undefined.');
    }
        
      
  
  }
  
}
