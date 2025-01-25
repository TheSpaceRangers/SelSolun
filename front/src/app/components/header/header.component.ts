import { Component } from '@angular/core';
import { NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { RegisterData } from '../../interfaces/register-data.interface';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgForOf
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  data:RegisterData = {
    username: '',
    email: '',
    password: ''
  };

  errorMessage: string[] = [];

  constructor(
    private authService: AuthService
  ) { }

  validateEmail() {
    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (this.data.email !== '' && !regex.test(this.data.email)) {
      this.errorMessage.push('Veuillez entrer une adresse email valide.');
    }
  }


}
