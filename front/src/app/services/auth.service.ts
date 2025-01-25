import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { catchError, throwError } from 'rxjs';

import { environment } from '../../environments/environment';
import { RegisterData } from '../interfaces/register-data.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private api_url: string = environment.api_url + 'api/auth/';

  constructor(
    private http: HttpClient
  ) { }

  register(data: RegisterData) {
    return this.http.post(
      this.api_url + 'register',
      data,
      {
        responseType: 'text',
        observe: 'response'
      }
    ).pipe(
      catchError(error => {
        return throwError(() => new Error(error.error?.error_message || 'Une erreur inattendue s’est produite.'));
      })
    );
  }
}
