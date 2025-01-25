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

  validatePassword() {
    const criteria = [
      { regex: /.{12,}/, message: '12 caractères minimum' },
      { regex: /[a-z]/, message: 'une lettre minuscule' },
      { regex: /[A-Z]/, message: 'une lettre majuscule' },
      { regex: /\d/, message: 'un chiffre' },
      { regex: /[^a-zA-Z0-9]/, message: 'un caractère spécial (ex. !@#$%^&*())' }
    ];

    const errors = criteria
      .filter(criteria => !criteria.regex.test(this.data.password))
      .map(criteria => criteria.message);

    if (errors.length > 0) {
      this.errorMessage.push(
        `Le mot de passe doit contenir : ${errors.join(', ')}.`
      );
    }
  }


}
