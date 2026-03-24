import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class MetricsService {
   private router = inject(Router);
     private http = inject(HttpClient);
  private readonly apiUrl = `${environment.url}/call`;
   
    public getDetails(id: string) {
    this.router.navigate(['/charts'], { queryParams: { analysisId: id } });
  }

  public async addRow(id: string) {
    console.log('clicked add new row with id:', id);
    const httpParams = { fileId: id };
return await this.http.post(`${this.apiUrl}/save-analysis`,null, {params: httpParams} );
  }
}
