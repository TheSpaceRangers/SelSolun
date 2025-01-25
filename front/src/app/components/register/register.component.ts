import { Component } from '@angular/core';
import { NgForOf, NgIf } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';

import { RegisterData } from '../../interfaces/register-data.interface';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgForOf
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  data:RegisterData = {
    username: '',
    email: '',
    password: ''
  };

  errorMessages: string[] = [];

  constructor(
    private authService: AuthService
  ) { }

  validateEmail() {
    this.errorMessages = [];

    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (this.data.email !== '' && !regex.test(this.data.email)) {
      this.errorMessages.push('Veuillez entrer une adresse email valide.');
    }
  }

  validatePassword() {
    this.errorMessages = [];

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
      this.errorMessages.push(
        `Le mot de passe doit contenir : ${errors.join(', ')}.`
      );
    }
  }

  onSubmit(register: NgForm) {
    if (register.valid && this.errorMessages.length === 0) {
      this.authService.register(this.data).subscribe({
        next: (response) => {
          console.log(response);
        },
        error: (err) => {
          console.log('Erreur lors de l’inscription :', err);
        }
      });
    } else {
      this.errorMessages.length === 0 ? this.errorMessages.push('Veuillez remplir tous les champs.') : null;
    }
  }

}
