import { Component } from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';
import { NgForOf, NgIf } from '@angular/common';

import { LoginData } from '../../interfaces/login-data.interface';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgForOf
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  data: LoginData = {
    email: '',
    password: ''
  };

  errorMessages: string[] = [];

  constructor(
    private authService: AuthService
  ) { }

  onSubmit(login: NgForm) {
    this.errorMessages = [];

    if (login.valid && this.errorMessages.length === 0) {
      this.authService.login(this.data).subscribe({
        next: (response) => {
          const token = response.body?.token;
          if (token)
            this.authService.loginIn(token)
        },
        error: (err) => {
          this.errorMessages.length === 0 ? this.errorMessages.push(err.message) : null;
        }
      });
    } else {
      this.errorMessages.length === 0 ? this.errorMessages.push('Veuillez remplir tous les champs.') : null;
    }
  }
}
