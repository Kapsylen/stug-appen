import {
  DestroyRef,
  effect,
  inject,
  Injectable,
  Injector,
  runInInjectionContext,
  signal
} from '@angular/core';
import {Kontakt, NewKontakt} from '../model/kontakt';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenService} from './token.service';

@Injectable({providedIn: 'root'})
export class KontaktService {

  private kontakter = signal<Kontakt[] | undefined>(undefined);
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
          this.fetchKontakter();
        }
      });
    });
  }

  fetchKontakter() {
    const token = this.tokenService.fetchToken();
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token?.access_token}`);
    const subscription = this.httpClient
      .get<Kontakt[]>(this.baseUrl + '/kontakt', {
        observe: 'body',
        responseType: 'json',
        headers: headers
      })
      .subscribe({
        next: (kontakterData) => {
          this.kontakter.set(kontakterData);
        }
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  getKontakter() {
    return this.kontakter;
  }

  deleteKontakt(id: string) {
    this.kontakter.update(signalKontakt => signalKontakt?.filter(k => k.id !== id));
    this.httpClient.delete(this.baseUrl + '/' + id).subscribe();
    console.log('Kontakt deleted successfully');
  }

  saveKontakt(kontak: NewKontakt) {
    this.httpClient.post<Kontakt>(this.baseUrl, kontak)
      .subscribe({
        next: (newKontaktData) => {
          console.log({...newKontaktData});
          this.kontakter.update(kontakList => [
            ...(kontakList ?? []),
            newKontaktData
          ]);
        }
      })
  }
}
