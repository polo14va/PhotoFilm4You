import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-password-modal',
  templateUrl: './password-modal.component.html',
  styleUrls: ['./password-modal.component.css']
})
export class PasswordModalComponent {

  constructor(private router: Router) { }


  public closeModal() {
    this.router.navigate(['/']);
  }
}
