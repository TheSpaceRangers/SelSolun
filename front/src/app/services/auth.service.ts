import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';

import { BehaviorSubject, catchError, Observable, throwError } from 'rxjs';

import { environment } from '../../environments/environment';
import { RegisterData } from '../interfaces/register-data.interface';
import { LoginData } from '../interfaces/login-data.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private api_url: string = environment.api_url + 'api/v1/auth';

  private authStatus = new BehaviorSubject<boolean>(false);

  constructor(
    private http: HttpClient
  ) { }

  register(data: RegisterData) {
    return this.http.post(
      `${this.api_url}/register`,
      data,
      {
        headers: {
          'Content-Type': 'application/json'
        },
        responseType: 'text',
        observe: 'response'
      }
    ).pipe(
      catchError(error => {
        console.error('Erreur dans le service:', error);
        return throwError(() => new Error(error.error?.error_message || 'Une erreur inattendue s’est produite.'));
      })
    );
  }

  login(data: LoginData): Observable<HttpResponse<{ token: string }>> {
    return this.http.post<{ token: string }>(
      `${this.api_url}/login`,
      data,
      {
        observe: 'response'
      }
    ).pipe(
      catchError(error => {
        return throwError(() => ({
          status: error.status,
          message: error.error || 'Une erreur inattendue s’est produite.',
          fullError: error
        }));
      })
    );
  }

  get isAuthenticated(): Observable<boolean> {
    return this.authStatus.asObservable();
  }

  loginIn(token: string) {
    localStorage.setItem('auth_token', token);
    this.authStatus.next(true);
  }

  logout() {
    localStorage.removeItem('auth_token');
    this.authStatus.next(false);
  }
}
