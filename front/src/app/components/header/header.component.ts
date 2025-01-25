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


}
