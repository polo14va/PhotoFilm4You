import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user/user.service';


@Component({
  selector: 'app-user-panel',
  templateUrl: './user-panel.component.html',
  styles: []
})
export class UserPanelComponent {
  user: User | undefined;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const userJson = sessionStorage.getItem('USER');
    if (userJson) {
      this.user = JSON.parse(userJson);

      if (this.user) {
        this.user.address = this.user.address ?? 'No declarado';
        this.user.city = this.user.city ?? 'No declarado';
        this.user.sendEmail = this.user.sendEmail ?? false;
      }
    } else {
      console.error("No se encontró información de usuario en sessionStorage");
    }
  }

  updateUser() {
    this.userService.updateUser(this.user!).subscribe({
      next: httpResponse => {
        alert('Nombre actualizado correctamente');
        this.refreshComponent();
      },
        error: error => this.handleLoginError(error)
      });
  }
  refreshComponent() {
    location.reload();
  }

  navigateToChangePassword() {

    this.userService.sendChangePasswordEmail().subscribe({
      next: httpResponse => alert(httpResponse),
      error: error => this.handleLoginError(error)
    });
  };


    // Handle login error
    private handleLoginError(error: { status: number; }) {
      
    }
}
