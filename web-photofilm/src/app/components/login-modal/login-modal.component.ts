import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-modal',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.css']
})
export class LoginModalComponent {
  
  constructor(private router: Router) { }


  public closeModal() {
    this.router.navigate(['/']);
  }
}
