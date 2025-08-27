import {
  DestroyRef,
  effect,
  inject,
  Injectable,
  Injector,
  runInInjectionContext,
  signal
} from '@angular/core';
import {NewUtlagg, Utlagg} from '../model/utlagg';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenService} from './token.service';

@Injectable({providedIn: 'root'})
export class UtlaggService {

  private utlagg =  signal<Utlagg[] | undefined>(undefined);
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
          this.fetchUtlagg();
        }
      });
    });
  }

  fetchUtlagg() {
    const token = this.tokenService.fetchToken();
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token?.access_token}`);

    const subscription = this.httpClient
      .get<Utlagg[]>(this.baseUrl + '/utlagg', {
        observe: 'body',
        responseType: 'json',
        headers: headers
      })
      .subscribe({
        next: (utlaggData) => {
          this.utlagg.set(utlaggData);
        }
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  getUtlagg() {
    return this.utlagg;
  }

  deleteUtlagg(id: string) {
    this.httpClient.delete(this.baseUrl + '/' + id).subscribe();
    this.utlagg.update(signalUtlagg => signalUtlagg?.filter(u => u.id !== id));
    console.log('Utlagg deleted successfully');
  }

  saveUtlagg(newUtlagg: NewUtlagg) {
    this.httpClient.post<Utlagg>(this.baseUrl, newUtlagg)
      .subscribe({
        next: (newUtlaggData) => {
          console.log({...newUtlaggData});
          this.utlagg.update(utlaggList => [
            ...(utlaggList ?? []),
            newUtlaggData
          ]);
        },
        error: (error) => {
          console.error('Failed to save utlagg:', error);
        }
      });
  }

  editUtlagg(utlagg: Utlagg) {
    this.httpClient.put<Utlagg>(this.baseUrl + '/' + utlagg.id, utlagg)
      .subscribe({
        next: (utlaggData) => {
          console.log('Utlagg updated: ' + utlaggData);
        // TBD
        //  this.utlagg = this.utlagg.map(u => u.id === utlaggData.id ? utlaggData : u);
        }
      });
  }
}
