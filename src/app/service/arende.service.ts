import {Arende, NewArende} from '../model/arenden';
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
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenService} from './token.service';

@Injectable({providedIn: 'root'})
export class ArendeService {

  private arenden = signal<Arende[] | undefined>(undefined);
  private destroyRef =inject(DestroyRef);
  private baseUrl = 'http://localhost:8081/api/v1';
  private tokenService = inject(TokenService);

  constructor(
    private httpClient: HttpClient,
    private injector: Injector,
  ) {
    runInInjectionContext(this.injector, () => {
      effect(() => {
        const token = this.tokenService.fetchToken();
        if (token) {
          this.fetchArende();
        }
      });
    });

  }

  fetchArende() {
    const token = this.tokenService.fetchToken();
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token?.access_token}`);

    const subscription = this.httpClient
      .get<Arende[]>(this.baseUrl + '/arende', {
        observe: 'body',
        responseType: 'json',
        headers: headers
      })
      .subscribe({
        next: (arendenData) => {
          this.arenden.set(arendenData);
        }
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  deleteArenden(id: string) {
    this.httpClient.delete(this.baseUrl + '/' + id).subscribe();
    this.arenden.update(arendeData => arendeData?.filter(a => a.id !== id));
    console.log('Arenden deleted successfully');
  }

  saveArende(newArende: NewArende) {
    console.log(newArende);
    this.httpClient.post<Arende>(this.baseUrl, newArende)
      .subscribe({
        next: (newArendeData) => {
          console.log({...newArendeData});
          this.arenden.update(arendenList => [
            ...(arendenList ?? []),
            newArendeData
          ])
        },
        error: (error) => {
          console.error('Failed to save arenden:', error);
        }
      })
  }

  editArende(arende: Arende) {
    this.httpClient.put(this.baseUrl + '/' + arende.id, arende)
      .subscribe({
        next: (arendeData) => {
          console.log('Arenden updated: ' + arendeData);
        }
      });
  }

  getArenden() {
    return this.arenden;
  }
}

