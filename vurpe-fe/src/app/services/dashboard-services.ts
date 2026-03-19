import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { map, catchError } from 'rxjs/operators';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DashboardServices {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.url}/call`;

  // La funzione restituisce un Observable che "emetterà" un array
  public getStats(): Observable<any[]> {
    return this.http.get<any>(`${this.apiUrl}/dashboard-stats`).pipe(
      map(resp => {
        if (resp?.status === "OK" && resp?.payload) {
          return resp.payload; // Trasformiamo la risposta per dare solo il payload
        }
        return [];
      }),
      catchError(err => {
        console.error('Error :', err);
        return of([]); // In caso di errore restituiamo un array vuoto
      })
    );
  }
}