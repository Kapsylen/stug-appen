import {
  afterNextRender,
  DestroyRef,
  effect,
  inject,
  Injectable,
  Injector,
  runInInjectionContext,
  signal
} from '@angular/core';
import {Faktura, NewFaktura} from '../model/faktura';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenService} from './token.service';

@Injectable({providedIn: 'root'})
export class FakturaService {

  private fakturor =  signal<Faktura[] | undefined>(undefined);
  private baseUrl = 'http://localhost:8081/api/v1';
  private destroyRef = inject(DestroyRef);
  private tokenService = inject(TokenService);

  constructor(
    private httpClient: HttpClient,
    private injector: Injector,
  ) {
    runInInjectionContext(this.injector, () => {
      effect(() => {
        const token = this.tokenService.fetchToken();
        if (token) {
          this.fetchFakturor();
        }
      });
    });

  }

  fetchFakturor() {
    const token = this.tokenService.fetchToken();
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token?.access_token}`);
    const subscription = this.httpClient
      .get<Faktura[]>(this.baseUrl + '/faktura', {
        observe: 'body',
        responseType: 'json',
        headers: headers
      })
    .subscribe({
        next: (fakturorData) => {
          this.fakturor.set(fakturorData);
        }
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  getFakturor() {
    return this.fakturor
  }

  deleteFakturor(id: string) {
    this.httpClient.delete(this.baseUrl + '/' + id).subscribe();
    this.fakturor.update(fakturorList => fakturorList?.filter(f => f.id !== id));
    console.log('Fakturor deleted successfully');
  }

  saveFaktura(newFakture: NewFaktura) {
    console.log(newFakture);
    this.httpClient.post<Faktura>(this.baseUrl, newFakture)
      .subscribe({
        next: (newFakturorData) => {
          console.log({...newFakturorData});
          this.fakturor.update(fakturorList => [
            ...(fakturorList ?? []),
            newFakturorData
          ]);
        },
        error: (error) => {
          console.error('Failed to save fakturor:', error);
          // Handle error appropriately
        }
      })
  }

  editFaktura(faktura: Faktura) {
    this.httpClient.put<Faktura>(this.baseUrl + '/' + faktura.id, faktura)
      .subscribe({
        next: (fakturaData) => {
          console.log('Fakturor updated: ' + fakturaData);
        // TBD
        //  this.fakturor = this.fakturor.map(f => f.id === fakturaData.id ? fakturaData : f);
        }
      });
  }
}
