import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-modal',
  templateUrl: './register-modal.component.html',
  styleUrl: './register-modal.component.css'
})
export class RegisterModalComponent {
  
  constructor(private router: Router) { }


  public closeModal() {
    this.router.navigate(['/']);
  }
}
