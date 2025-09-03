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
import {HttpClient} from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class KontaktService {

  private kontakter = signal<Kontakt[] | undefined>(undefined);
  private baseUrl = 'http://localhost:8081/api/v1/kontakt';
  private destroyRef = inject(DestroyRef);

  constructor(
    private httpClient: HttpClient,
    private injector: Injector,
  ) {
    runInInjectionContext(this.injector, () => {
      effect(() => {
        this.fetchKontakter();
      });
    });
  }

  fetchKontakter() {
    const subscription = this.httpClient
      .get<Kontakt[]>(this.baseUrl, {
        observe: 'body',
        responseType: 'json'
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
