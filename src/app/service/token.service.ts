import {DestroyRef, inject, Injectable, Injector, runInInjectionContext, signal} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenResponse} from '../model/token';

@Injectable({providedIn: 'root'})
export class TokenService {

  private baseUrl = 'http://localhost:8081/api/v1/token';
  private destroyRef =inject(DestroyRef);
  private token = signal<TokenResponse | undefined>(undefined);

  constructor(
    private httpClient: HttpClient,
    private injector: Injector,
  ) {
    runInInjectionContext(this.injector, () => {
      try {
        this.fetchToken();
      } catch (e) {
        console.error('Failed to fetch bearer token', e);
      }
    });
  }

   fetchToken() {
    const headers = new HttpHeaders()
      .set('grantType', 'password')
      .set('clientId', 'stug-rest-api')
      .set('username', 'sebsve')
      .set('password', 'sebsve');

    const subscription = this.httpClient
      .get<TokenResponse>(this.baseUrl, {
        observe: 'body',
        responseType: 'json',
        headers: headers,
      })
      .subscribe({
        next: (token) => {
          this.token.set(token);
        },
        error: (error) => {
          console.error('Failed to fetch bearer token', error);
        }
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
    return this.token();
  }
}

